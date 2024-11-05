package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.EstadoReserva;
import org.example.entidades.Reserva;

public class DeleteReservaPanel extends JPanel {
    private JTextField codReservaField;
    private JTextArea detalleReservaArea;
    private CRUDController crudController;

    public DeleteReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        codReservaField = new JTextField(10);
        inputPanel.add(new JLabel("Código de la Reserva a eliminar:"));
        inputPanel.add(codReservaField);

        JButton showDetailsButton = new JButton("Mostrar Detalles");
        showDetailsButton.addActionListener(e -> mostrarDetallesReserva());
        inputPanel.add(showDetailsButton);

        JButton deleteButton = new JButton("Eliminar Reserva");
        deleteButton.addActionListener(e -> eliminarReserva());
        inputPanel.add(deleteButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        // Área de texto para mostrar detalles de la reserva
        detalleReservaArea = new JTextArea(8, 30);
        detalleReservaArea.setEditable(false);
        detalleReservaArea.setBorder(BorderFactory.createTitledBorder("Detalles de la Reserva"));
        add(new JScrollPane(detalleReservaArea), BorderLayout.CENTER);
    }

    private void mostrarDetallesReserva() {
        try {
            int codReserva = Integer.parseInt(codReservaField.getText());
            Reserva reserva = crudController.readReserva(codReserva);

            if (reserva != null && reserva.getEstadoReserva() != EstadoReserva.CANCELADO) {
                detalleReservaArea.setText(""); // Limpiar el área de detalles
                detalleReservaArea.append("Código de Reserva: " + reserva.getCodReserva() + "\n");
                detalleReservaArea.append("Check-in: " + reserva.getCheckin() + "\n");
                detalleReservaArea.append("Check-out: " + reserva.getCheckout() + "\n");
                detalleReservaArea.append("Estado: " + reserva.getEstadoReserva() + "\n");
                detalleReservaArea.append("Tarifa: " + reserva.getTarifa() + "\n");
                detalleReservaArea.append("ID Hotel: " + reserva.getIdHotel() + "\n");
                detalleReservaArea.append("ID Habitación: " + reserva.getIdHabitacion() + "\n");
                detalleReservaArea.append("ID Huésped: " + reserva.getIdHuesped() + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la reserva o está en estado CANCELADO.", "Error", JOptionPane.ERROR_MESSAGE);
                detalleReservaArea.setText(""); // Limpiar el área de detalles si no se muestra
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de reserva válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarReserva() {
        try {
            int codReserva = Integer.parseInt(codReservaField.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cancelar esta reserva?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                crudController.deleteReserva(codReserva);
                JOptionPane.showMessageDialog(this, "Reserva cancelada exitosamente.");
                detalleReservaArea.setText(""); // Limpiar el área de detalles después de cancelar
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de reserva válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al cancelar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
