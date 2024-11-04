package org.example.PRUEBA;

import org.example.controlador.CRUDController;
import org.example.entidades.EstadoReserva;
import org.example.entidades.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReservaCrudTest {
    public static void main(String[] args) throws ParseException {
        // Inicializar el controlador CRUD
        CRUDController crudController = new CRUDController();

        int codReserva = crudController.getUltimoCodReserva() + 1; // Trae el ultimo ID existente de las reservas (coleccion contadores) y lo incrementa

        // Crear una nueva reserva con datos de prueba
        Reserva nuevaReserva = new Reserva(
                codReserva,                     // Código de la reserva
                new SimpleDateFormat("yyyy-MM-dd").parse("2023-10-01"),                   // Fecha de check-in
                new SimpleDateFormat("yyyy-MM-dd").parse("2023-10-10"),                   // Fecha de check-out
                EstadoReserva.CONFIRMADO,                   // Estado de la reserva
                1500.0,                         // Tarifa
                1,                 // ID del hotel (generar un nuevo ObjectId)
                101,                            // ID de la habitación
                1                  // ID del huésped (generar un nuevo ObjectId)
        );

        // Intentar crear la reserva en MongoDB y Neo4j
        System.out.println("Intentando crear la reserva: " + nuevaReserva);
        crudController.createReserva(nuevaReserva);
        System.out.println("Reserva creada exitosamente.");

        // Leer la reserva de MongoDB para verificar que se guardó correctamente
        Reserva reservaGuardada = crudController.readReserva(codReserva);
        if (reservaGuardada != null) {
            System.out.println("Reserva recuperada de MongoDB: " + reservaGuardada);
        } else {
            System.out.println("No se encontró la reserva en MongoDB.");
        }

        System.out.println("Ahora, se va a actualizar la reserva con otros datos ...");

        // Actualizar la reserva con nuevos datos
        nuevaReserva.setCheckin(new SimpleDateFormat("yyyy-MM-dd").parse("2023-10-05"));
        nuevaReserva.setCheckout(new SimpleDateFormat("yyyy-MM-dd").parse("2023-10-15"));
        nuevaReserva.setEstadoReserva(EstadoReserva.CANCELADO);
        nuevaReserva.setTarifa(1900.0);

        // Llamada al metodo para actualizar los datos de la reserva
        crudController.updateReserva(nuevaReserva);
        System.out.println("Reserva actualizada con éxito.");

        // Leer la reserva de MongoDB para verificar que se actualizó correctamente
        Reserva reservaActualizada = crudController.readReserva(codReserva);
        if (reservaActualizada != null) {
            System.out.println("Reserva actualizada recuperada de MongoDB: " + reservaActualizada);
        } else {
            System.out.println("No se encontró la reserva actualizada en MongoDB.");
        }

        // Eliminar la reserva de MongoDB
        crudController.deleteReserva(codReserva);
        System.out.println("Reserva eliminada de MongoDB con cod_reserva: " + codReserva);

        // Verificar que la reserva fue eliminada de MongoDB
        Reserva reservaEliminadaMongo = crudController.readReserva(codReserva);
        if (reservaEliminadaMongo == null) {
            System.out.println("Confirmación: la reserva con cod_reserva " + codReserva + " ya no existe en MongoDB.");
        } else {
            System.out.println("Error: la reserva aún existe en MongoDB.");
        }
    }
}