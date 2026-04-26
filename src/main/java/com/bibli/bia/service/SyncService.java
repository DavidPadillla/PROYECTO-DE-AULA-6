package com.bibli.bia.service;

import com.bibli.bia.Model.*;
import com.bibli.bia.repository.*;
import com.bibli.bia.postgres.entity.*;
import com.bibli.bia.postgres.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SyncService {


    private static final int LIMITE_REGISTROS = 10;


    private static LocalDateTime ultimaSincronizacion = null;
    private static int totalSincronizados = 0;

    @Autowired
    private LibroRepository libroMongoRepo;
    @Autowired
    private LibroFisicoRepository libroFisicoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private ReservaRepository reservaRepo;
    @Autowired
    private Resenarepository resenaRepo;
    @Autowired
    private ProgresoLecturaRepository progresoRepo;
    @Autowired
    private MultaRepository multaRepo;
    @Autowired
    private RespuestaDashboardRepository dashboardRepo;

    @Autowired
    private LibroPostgresRepository libroPostgresRepo;
    @Autowired
    private LibroFisicoPostgresRepository libroFisicoPostgresRepo;
    @Autowired
    private UsuarioPostgresRepository usuarioPostgresRepo;
    @Autowired
    private ReservaPostgresRepository reservaPostgresRepo;
    @Autowired
    private ResenaPostgresRepository resenaPostgresRepo;
    @Autowired
    private ProgresoLecturaPostgresRepository progresoPostgresRepo;
    @Autowired
    private MultaPostgresRepository multaPostgresRepo;
    @Autowired
    private RespuestaDashboardPostgresRepository dashboardPostgresRepo;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void sincronizarTodo() {
        System.out.println("✅ Iniciando sincronización MongoDB → PostgreSQL...");
        System.out.println("📊 Limitando a " + LIMITE_REGISTROS + " registros por tabla");

        sincronizarLibros();
        sincronizarLibrosFisicos();
        sincronizarUsuarios();
        sincronizarReservas();
        sincronizarResenas();
        sincronizarProgresoLectura();
        sincronizarMultas();
        sincronizarDashboard();

        // Actualizar estadísticas
        ultimaSincronizacion = LocalDateTime.now();

        System.out.println("🎉 Sincronización completada!");
        System.out.println("📊 Total de registros sincronizados: " + totalSincronizados);
        System.out.println("📅 Última sincronización: " + ultimaSincronizacion);
    }

    private void sincronizarLibros() {
        List<LibroPostgres> list = new ArrayList<>();
        int count = 0;

        for (LibroModel m : libroMongoRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = libroPostgresRepo.findAll().stream()
                    .anyMatch(l -> l.getTitulo() != null && l.getTitulo().equals(m.getTitulo()));

            if (!existe) {
                LibroPostgres p = new LibroPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setTitulo(m.getTitulo());
                p.setUrl(m.getUrl());
                p.setAutor(m.getAutor());
                p.setDescripcion(m.getDescripcion());
                p.setCategoria(m.getCategoria());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            libroPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("📚 Sincronizados " + list.size() + " libros nuevos (máximo " + LIMITE_REGISTROS + ")");
    }

    private void sincronizarLibrosFisicos() {
        List<LibroFisicoPostgres> list = new ArrayList<>();
        int count = 0;

        for (LibroFisicoModel m : libroFisicoRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = libroFisicoPostgresRepo.findAll().stream()
                    .anyMatch(l -> l.getTitulo() != null && l.getTitulo().equals(m.getTitulo()));

            if (!existe) {
                LibroFisicoPostgres p = new LibroFisicoPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setTitulo(m.getTitulo());
                p.setAutor(m.getAutor());
                p.setDescripcion(m.getDescripcion());
                p.setCategoria(m.getCategoria());
                p.setStock(m.getStock());
                p.setReservado(m.getReservado());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            libroFisicoPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("📖 Sincronizados " + list.size() + " libros físicos nuevos");
    }

    private void sincronizarUsuarios() {
        List<UsuarioPostgres> list = new ArrayList<>();
        int count = 0;

        for (Usuario m : usuarioRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = usuarioPostgresRepo.findAll().stream()
                    .anyMatch(u -> u.getUsername() != null && u.getUsername().equals(m.getUsername()));

            if (!existe) {
                UsuarioPostgres p = new UsuarioPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setUsername(m.getUsername());
                p.setPassword(m.getPassword());
                try {
                    p.setRoles(objectMapper.writeValueAsString(m.getRoles()));
                } catch (Exception e) {
                    p.setRoles("[]");
                }
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            usuarioPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("👤 Sincronizados " + list.size() + " usuarios nuevos");
    }

    private void sincronizarReservas() {
        List<ReservaPostgres> list = new ArrayList<>();
        int count = 0;

        for (ReservaModel m : reservaRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = reservaPostgresRepo.findAll().stream()
                    .anyMatch(r -> r.getIdUsuario() != null && r.getIdUsuario().equals(m.getIdUsuario())
                            && r.getLibro() != null && r.getLibro().equals(m.getLibro()));

            if (!existe) {
                ReservaPostgres p = new ReservaPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setIdUsuario(m.getIdUsuario());
                p.setNombreCompleto(m.getNombreCompleto());
                p.setCorreo(m.getCorreo());
                p.setCategoria(m.getCategoria());
                p.setLibro(m.getLibro());
                p.setFecha(m.getFecha());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            reservaPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("📅 Sincronizados " + list.size() + " reservas nuevas");
    }

    private void sincronizarResenas() {
        List<ResenaPostgres> list = new ArrayList<>();
        int count = 0;

        for (ResenaModel m : resenaRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = resenaPostgresRepo.findAll().stream()
                    .anyMatch(r -> r.getNombre() != null && r.getNombre().equals(m.getNombre())
                            && r.getComentario() != null && r.getComentario().equals(m.getComentario()));

            if (!existe) {
                ResenaPostgres p = new ResenaPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setNombre(m.getNombre());
                p.setComentario(m.getComentario());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            resenaPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("⭐ Sincronizadas " + list.size() + " reseñas nuevas");
    }

    private void sincronizarProgresoLectura() {
        List<ProgresoLecturaPostgres> list = new ArrayList<>();
        int count = 0;

        for (ProgresoLectura m : progresoRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = progresoPostgresRepo.findAll().stream()
                    .anyMatch(p -> p.getUsername() != null && p.getUsername().equals(m.getUsername()));

            if (!existe) {
                ProgresoLecturaPostgres p = new ProgresoLecturaPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setUsername(m.getUsername());
                p.setTotalLibrosLeidos(m.getTotalLibrosLeidos());
                p.setUltimaActualizacion(m.getUltimaActualizacion());
                p.setPuntos(m.getPuntos());
                try {
                    p.setLibrosCompletados(objectMapper.writeValueAsString(m.getLibrosCompletados()));
                    p.setCapitulosPorLibro(objectMapper.writeValueAsString(m.getCapitulosPorLibro()));
                } catch (Exception e) {
                    p.setLibrosCompletados("[]");
                    p.setCapitulosPorLibro("{}");
                }
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            progresoPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("📊 Sincronizados " + list.size() + " progresos de lectura nuevos");
    }

    // ✅ CORREGIDO: No asignar ID manualmente, usar constructor
    private void sincronizarMultas() {
        List<MultaPostgres> list = new ArrayList<>();
        int count = 0;

        for (MultaModel m : multaRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = multaPostgresRepo.findAll().stream()
                    .anyMatch(mt -> mt.getIdUsuario() != null && mt.getIdUsuario().equals(m.getIdUsuario())
                            && mt.getLibro() != null && mt.getLibro().equals(m.getLibro()));

            if (!existe) {
                // ✅ Usar el constructor sin ID (PostgreSQL lo genera automáticamente)
                MultaPostgres p = new MultaPostgres(
                        m.getIdUsuario(),
                        m.getNombreUsuario(),
                        m.getLibro(),
                        m.getFechaReserva(),
                        m.getFechaDevolucion(),
                        m.getDiasRetraso(),
                        m.getValorMulta()
                );

                p.setPagada(m.isPagada());
                p.setFechaPago(m.getFechaPago());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            multaPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("💰 Sincronizadas " + list.size() + " multas nuevas");
    }

    private void sincronizarDashboard() {
        List<RespuestaDashboardPostgres> list = new ArrayList<>();
        int count = 0;

        for (RespuestaDashboard m : dashboardRepo.findAll()) {
            if (count >= LIMITE_REGISTROS) break;

            boolean existe = dashboardPostgresRepo.findAll().stream()
                    .anyMatch(d -> d.getGenero() != null && d.getGenero().equals(m.getGenero())
                            && d.getEdad() != null && d.getEdad().equals(m.getEdad()));

            if (!existe) {
                RespuestaDashboardPostgres p = new RespuestaDashboardPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setEdad(m.getEdad());
                p.setGenero(m.getGenero());
                p.setEducacion(m.getEducacion());
                p.setFrecuencia(m.getFrecuencia());
                p.setCategoriaFavorita(m.getCategoriaFavorita());
                p.setFormato(m.getFormato());
                p.setUso(m.getUso());
                p.setLibrosMes(m.getLibrosMes());
                p.setCalificacion(m.getCalificacion());
                p.setRecomendacion(m.getRecomendacion());
                p.setDispositivos(m.getDispositivos());
                p.setUltimoLibro(m.getUltimoLibro());
                p.setMejoras(m.getMejoras());
                p.setRecomendaciones(m.getRecomendaciones());
                p.setClubes(m.getClubes());
                p.setCompras(m.getCompras());
                p.setAutoresFavoritos(m.getAutoresFavoritos());
                p.setBoletines(m.getBoletines());
                p.setFechaRegistro(m.getFechaRegistro());
                list.add(p);
                count++;
            }
        }

        if (!list.isEmpty()) {
            dashboardPostgresRepo.saveAll(list);
            totalSincronizados += list.size();
        }
        System.out.println("📋 Sincronizadas " + list.size() + " respuestas dashboard nuevas");
    }

    // ==================== MÉTODOS PARA SINCRONIZACIÓN EN TIEMPO REAL ====================

    public void sincronizarUsuario(Usuario usuario) {
        try {
            boolean existe = usuarioPostgresRepo.findAll().stream()
                    .anyMatch(u -> u.getUsername() != null && u.getUsername().equals(usuario.getUsername()));

            if (!existe) {
                UsuarioPostgres p = new UsuarioPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setUsername(usuario.getUsername());
                p.setPassword(usuario.getPassword());
                try {
                    p.setRoles(objectMapper.writeValueAsString(usuario.getRoles()));
                } catch (Exception e) {
                    p.setRoles("[]");
                }
                usuarioPostgresRepo.save(p);
                System.out.println("✅ Usuario sincronizado a Neon: " + usuario.getUsername());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando usuario: " + e.getMessage());
        }
    }

    public void sincronizarLibro(LibroModel libro) {
        try {
            boolean existe = libroPostgresRepo.findAll().stream()
                    .anyMatch(l -> l.getTitulo() != null && l.getTitulo().equals(libro.getTitulo()));
            if (!existe) {
                LibroPostgres p = new LibroPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setTitulo(libro.getTitulo());
                p.setUrl(libro.getUrl());
                p.setAutor(libro.getAutor());
                p.setDescripcion(libro.getDescripcion());
                p.setCategoria(libro.getCategoria());
                libroPostgresRepo.save(p);
                System.out.println("✅ Libro sincronizado a Neon: " + libro.getTitulo());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando libro: " + e.getMessage());
        }
    }

    public void sincronizarLibroFisico(LibroFisicoModel libro) {
        try {
            boolean existe = libroFisicoPostgresRepo.findAll().stream()
                    .anyMatch(l -> l.getTitulo() != null && l.getTitulo().equals(libro.getTitulo()));
            if (!existe) {
                LibroFisicoPostgres p = new LibroFisicoPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setTitulo(libro.getTitulo());
                p.setAutor(libro.getAutor());
                p.setDescripcion(libro.getDescripcion());
                p.setCategoria(libro.getCategoria());
                p.setStock(libro.getStock());
                p.setReservado(libro.getReservado());
                libroFisicoPostgresRepo.save(p);
                System.out.println("✅ Libro físico sincronizado a Neon: " + libro.getTitulo());
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando libro físico: " + e.getMessage());
        }
    }

    public void sincronizarReserva(ReservaModel reserva) {
        try {
            boolean existe = reservaPostgresRepo.findAll().stream()
                    .anyMatch(r -> r.getIdUsuario() != null && r.getIdUsuario().equals(reserva.getIdUsuario())
                            && r.getLibro() != null && r.getLibro().equals(reserva.getLibro()));
            if (!existe) {
                ReservaPostgres p = new ReservaPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setIdUsuario(reserva.getIdUsuario());
                p.setNombreCompleto(reserva.getNombreCompleto());
                p.setCorreo(reserva.getCorreo());
                p.setCategoria(reserva.getCategoria());
                p.setLibro(reserva.getLibro());
                p.setFecha(reserva.getFecha());
                reservaPostgresRepo.save(p);
                System.out.println("✅ Reserva sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando reserva: " + e.getMessage());
        }
    }

    public void sincronizarResena(ResenaModel resena) {
        try {
            boolean existe = resenaPostgresRepo.findAll().stream()
                    .anyMatch(r -> r.getNombre() != null && r.getNombre().equals(resena.getNombre())
                            && r.getComentario() != null && r.getComentario().equals(resena.getComentario()));
            if (!existe) {
                ResenaPostgres p = new ResenaPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setNombre(resena.getNombre());
                p.setComentario(resena.getComentario());
                resenaPostgresRepo.save(p);
                System.out.println("✅ Reseña sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando reseña: " + e.getMessage());
        }
    }

    // ✅ CORREGIDO: No asignar ID manualmente, usar constructor
    public void sincronizarMulta(MultaModel multa) {
        try {
            boolean existe = multaPostgresRepo.findAll().stream()
                    .anyMatch(m -> m.getIdUsuario() != null && m.getIdUsuario().equals(multa.getIdUsuario())
                            && m.getLibro() != null && m.getLibro().equals(multa.getLibro()));
            if (!existe) {
                // ✅ Usar el constructor sin ID (PostgreSQL lo genera automáticamente)
                MultaPostgres p = new MultaPostgres(
                        multa.getIdUsuario(),
                        multa.getNombreUsuario(),
                        multa.getLibro(),
                        multa.getFechaReserva(),
                        multa.getFechaDevolucion(),
                        multa.getDiasRetraso(),
                        multa.getValorMulta()
                );

                p.setPagada(multa.isPagada());
                p.setFechaPago(multa.getFechaPago());

                multaPostgresRepo.save(p);
                System.out.println("✅ Multa sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando multa: " + e.getMessage());
        }
    }

    public void sincronizarProgresoLectura(ProgresoLectura progreso) {
        try {
            boolean existe = progresoPostgresRepo.findAll().stream()
                    .anyMatch(p -> p.getUsername() != null && p.getUsername().equals(progreso.getUsername()));
            if (!existe) {
                ProgresoLecturaPostgres p = new ProgresoLecturaPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setUsername(progreso.getUsername());
                p.setTotalLibrosLeidos(progreso.getTotalLibrosLeidos());
                p.setUltimaActualizacion(progreso.getUltimaActualizacion());
                p.setPuntos(progreso.getPuntos());
                try {
                    p.setLibrosCompletados(objectMapper.writeValueAsString(progreso.getLibrosCompletados()));
                    p.setCapitulosPorLibro(objectMapper.writeValueAsString(progreso.getCapitulosPorLibro()));
                } catch (Exception e) {
                    p.setLibrosCompletados("[]");
                    p.setCapitulosPorLibro("{}");
                }
                progresoPostgresRepo.save(p);
                System.out.println("✅ Progreso lectura sincronizado a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando progreso lectura: " + e.getMessage());
        }
    }

    public void sincronizarRespuestaDashboard(RespuestaDashboard respuesta) {
        try {
            boolean existe = dashboardPostgresRepo.findAll().stream()
                    .anyMatch(d -> d.getGenero() != null && d.getGenero().equals(respuesta.getGenero())
                            && d.getEdad() != null && d.getEdad().equals(respuesta.getEdad()));
            if (!existe) {
                RespuestaDashboardPostgres p = new RespuestaDashboardPostgres();
                p.setId(UUID.randomUUID().toString());
                p.setEdad(respuesta.getEdad());
                p.setGenero(respuesta.getGenero());
                p.setEducacion(respuesta.getEducacion());
                p.setFrecuencia(respuesta.getFrecuencia());
                p.setCategoriaFavorita(respuesta.getCategoriaFavorita());
                p.setFormato(respuesta.getFormato());
                p.setUso(respuesta.getUso());
                p.setLibrosMes(respuesta.getLibrosMes());
                p.setCalificacion(respuesta.getCalificacion());
                p.setRecomendacion(respuesta.getRecomendacion());
                p.setDispositivos(respuesta.getDispositivos());
                p.setUltimoLibro(respuesta.getUltimoLibro());
                p.setMejoras(respuesta.getMejoras());
                p.setRecomendaciones(respuesta.getRecomendaciones());
                p.setClubes(respuesta.getClubes());
                p.setCompras(respuesta.getCompras());
                p.setAutoresFavoritos(respuesta.getAutoresFavoritos());
                p.setBoletines(respuesta.getBoletines());
                p.setFechaRegistro(respuesta.getFechaRegistro());
                dashboardPostgresRepo.save(p);
                System.out.println("✅ Respuesta dashboard sincronizada a Neon");
            }
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando respuesta dashboard: " + e.getMessage());
        }
    }

    // ==================== MÉTODOS PARA ESTADÍSTICAS ====================

    public static LocalDateTime getUltimaSincronizacion() {
        return ultimaSincronizacion;
    }

    public static int getTotalSincronizados() {
        return totalSincronizados;
    }
}