package com.umg.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umg.api.model.Tarea;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;

import org.apache.hc.client5.http.entity.EntityBuilder;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.List;

/**
 * Consume la API REST de Tareas:
 *   GET    /tareas          -> Listar todas las tareas
 *   POST   /tareas          -> Crear una nueva tarea
 *   PUT    /tareas/:id      -> Actualizar una tarea existente
 *   DELETE /tareas/:id      -> Eliminar una tarea
 */
public class TareaService {

    // ===== CAMBIAR EN EL PARCIAL: URL real que te dé el enunciado / Render =====
    private static final String BASE_URL =
            "https://apipractica2.onrender.com/tareas";

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public TareaService() {
    }

    // ==========================================================
    // GET /tareas - listar todas las tareas
    // ==========================================================
    public List<Tarea> getTareas() throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(BASE_URL);

            ClassicHttpResponse response = client.execute(request);

            int statusCode = response.getCode();

            if (statusCode != 200) {
                throw new Exception("Error GET. Código HTTP: " + statusCode);
            }

            InputStream is = response.getEntity().getContent();

            return mapper.readValue(is, new TypeReference<List<Tarea>>() {
            });
        }
    }

    // ==========================================================
    // POST /tareas - crear una nueva tarea
    // ==========================================================
    public Tarea createTarea(Tarea t) throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(BASE_URL);

            String json = mapper.writeValueAsString(t);

            request.setEntity(
                    EntityBuilder.create()
                            .setText(json)
                            .setContentType(ContentType.APPLICATION_JSON)
                            .build()
            );

            ClassicHttpResponse response = client.execute(request);

            int statusCode = response.getCode();

            if (statusCode != 200 && statusCode != 201) {
                throw new Exception("Error POST. Código HTTP: " + statusCode);
            }

            InputStream is = response.getEntity().getContent();

            return mapper.readValue(is, Tarea.class);
        }
    }

    // ==========================================================
    // PUT /tareas/:id - actualizar una tarea existente
    // ==========================================================
    public Tarea updateTarea(int id, Tarea t) throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPut request = new HttpPut(BASE_URL + "/" + id);

            String json = mapper.writeValueAsString(t);

            request.setEntity(
                    EntityBuilder.create()
                            .setText(json)
                            .setContentType(ContentType.APPLICATION_JSON)
                            .build()
            );

            ClassicHttpResponse response = client.execute(request);

            int statusCode = response.getCode();

            if (statusCode != 200) {
                throw new Exception("Error PUT. Código HTTP: " + statusCode);
            }

            InputStream is = response.getEntity().getContent();

            return mapper.readValue(is, Tarea.class);
        }
    }

    // ==========================================================
    // DELETE /tareas/:id - eliminar una tarea
    // ==========================================================
    public void deleteTarea(int id) throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpDelete request = new HttpDelete(BASE_URL + "/" + id);

            ClassicHttpResponse response = client.execute(request);

            int statusCode = response.getCode();

            if (statusCode != 200 && statusCode != 204) {
                throw new Exception("Error DELETE. Código HTTP: " + statusCode);
            }
        }
    }

    // ==========================================================
    // REPORTE (JasperReports) - carga el .jrxml desde el classpath,
    // NO desde una ruta absoluta.
    // ==========================================================
    public void reportTareas() {
        try {
            List<Tarea> datos = getTareas();

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);

            try (InputStream jrxmlStream =
                         Thread.currentThread().getContextClassLoader()
                                 .getResourceAsStream("reportes/tareas.jrxml")) {

                if (jrxmlStream == null) {
                    throw new Exception("No se encontró reports/tareas.jrxml en el classpath.");
                }

                JasperReport jr = JasperCompileManager.compileReport(jrxmlStream);
                JasperPrint jp = JasperFillManager.fillReport(jr, null, dataSource);
                JasperViewer.viewReport(jp, false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
