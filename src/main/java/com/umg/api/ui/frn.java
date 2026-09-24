package com.umg.api.ui;

import com.umg.api.model.Tarea;
import com.umg.api.service.TareaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CRUD de Tareas (ejercicio de la fotografía del parcial pasado).
 */
public class frn extends JFrame {

    private final TareaService service = new TareaService();

    private final String[] COLUMNAS = {
            "ID", "Titulo", "Descripcion", "Estado", "Responsable",
            "F.Inicio", "F.Fin", "F.Limite"
    };

    private DefaultTableModel model;
    private JTable tabla;

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtDescripcion;
    private JComboBox<String> cmbEstado;
    private JTextField txtResponsable;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtFechaLimite;

    private JButton btnCargar;
    private JButton btnGuardar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnReporte;

    public frn() {
        setTitle("Gestión de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(construirPanelFormulario(), BorderLayout.NORTH);
        add(construirPanelTabla(), BorderLayout.CENTER);
        add(construirPanelBotones(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        cargar();
    }

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la tarea"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(6);
        txtId.setEditable(false);

        txtTitulo = new JTextField(18);
        txtDescripcion = new JTextField(18);
        cmbEstado = new JComboBox<>(new String[]{"Pendiente", "En Progreso", "Completada"});
        txtResponsable = new JTextField(18);
        txtFechaInicio = new JTextField("yyyy-MM-dd", 10);
        txtFechaFin = new JTextField("yyyy-MM-dd", 10);
        txtFechaLimite = new JTextField("yyyy-MM-dd", 10);

        int fila = 0;
        agregarCampo(panel, gbc, fila++, "ID:", txtId);
        agregarCampo(panel, gbc, fila++, "Título:", txtTitulo);
        agregarCampo(panel, gbc, fila++, "Descripción:", txtDescripcion);
        agregarCampo(panel, gbc, fila++, "Estado:", cmbEstado);
        agregarCampo(panel, gbc, fila++, "Responsable:", txtResponsable);
        agregarCampo(panel, gbc, fila++, "Fecha inicio:", txtFechaInicio);
        agregarCampo(panel, gbc, fila++, "Fecha fin:", txtFechaFin);
        agregarCampo(panel, gbc, fila++, "Fecha límite:", txtFechaLimite);

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private JScrollPane construirPanelTabla() {
        model = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
        return new JScrollPane(tabla);
    }

    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());

        btnCargar = new JButton("Actualizar/Cargar");
        btnGuardar = new JButton("Guardar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnReporte = new JButton("Generar Reporte");

        btnCargar.addActionListener(e -> cargar());
        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnReporte.addActionListener(e -> generarReporte());

        panel.add(btnCargar);
        panel.add(btnGuardar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnReporte);

        return panel;
    }

    private void cargar() {
        try {
            List<Tarea> lista = service.getTareas();
            model.setRowCount(0);

            for (Tarea t : lista) {
                model.addRow(new Object[]{
                        t.getId(),
                        t.getTitulo(),
                        t.getDescripcion(),
                        t.getEstado(),
                        t.getResponsable(),
                        t.getFechaInicio(),
                        t.getFechaFin(),
                        t.getFechaLimite()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar tareas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            if (txtTitulo.getText().isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "El título es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Tarea t = leerFormulario();
            service.createTarea(t);
            cargar();
            limpiar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar tarea: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero una tarea de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(txtId.getText());
            Tarea t = leerFormulario();
            t.setId(id);
            service.updateTarea(id, t);
            cargar();
            limpiar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar tarea: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una tarea primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(fila, 0);
        String titulo = String.valueOf(model.getValueAt(fila, 1));

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar la tarea?\n\nID: " + id + "\nTítulo: " + titulo,
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            service.deleteTarea(id);
            cargar();
            limpiar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar tarea: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtTitulo.setText("");
        txtDescripcion.setText("");
        cmbEstado.setSelectedIndex(0);
        txtResponsable.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtFechaLimite.setText("");
        tabla.clearSelection();
    }

    private void generarReporte() {
        service.reportTareas();
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }
        txtId.setText(String.valueOf(model.getValueAt(fila, 0)));
        txtTitulo.setText(String.valueOf(model.getValueAt(fila, 1)));
        txtDescripcion.setText(String.valueOf(model.getValueAt(fila, 2)));
        cmbEstado.setSelectedItem(String.valueOf(model.getValueAt(fila, 3)));
        txtResponsable.setText(String.valueOf(model.getValueAt(fila, 4)));
        txtFechaInicio.setText(String.valueOf(model.getValueAt(fila, 5)));
        txtFechaFin.setText(String.valueOf(model.getValueAt(fila, 6)));
        txtFechaLimite.setText(String.valueOf(model.getValueAt(fila, 7)));
    }

    private Tarea leerFormulario() {
        Tarea t = new Tarea();
        t.setTitulo(txtTitulo.getText());
        t.setDescripcion(txtDescripcion.getText());
        t.setEstado((String) cmbEstado.getSelectedItem());
        t.setResponsable(txtResponsable.getText());
        t.setFechaInicio(txtFechaInicio.getText());
        t.setFechaFin(txtFechaFin.getText());
        t.setFechaLimite(txtFechaLimite.getText());
        return t;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        EventQueue.invokeLater(() -> new frn().setVisible(true));
    }
}
