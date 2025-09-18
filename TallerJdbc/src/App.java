import Service.EstudianteService;
import model.Estudiante;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final EstudianteService service = new EstudianteService();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n===== MENÚ =====");
            System.out.println("1. Ingresar Estudiante");
            System.out.println("2. Actualizar datos de Estudiante");
            System.out.println("3. Eliminar Estudiante");
            System.out.println("4. Consultar todos los estudiantes");
            System.out.println("5. Consultar estudiante por correo");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerEnteroValido("", sc, " Opción inválida. Ingrese un número.");

            switch (opcion) {
                case 1 -> insertarEstudiante();
                case 2 -> actualizarEstudiante();
                case 3 -> eliminarEstudiante();
                case 4 -> listarEstudiantes();
                case 5 -> buscarPorCorreo();
                case 6 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida. Por favor Intente de nuevo.");
            }
        } while (opcion != 6);
    }

    // ================= MÉTODOS DEL MENÚ =================

    private static void insertarEstudiante() {
        System.out.println("\n--- Ingresar Estudiante ---");

        String nombre = leerTextoValido("Ingrese nombre (0 para regresar): ", sc, "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$", 
                                        " Nombre inválido. Solo letras y espacios.");
        if (nombre.equals("0")) return;

        String apellido = leerTextoValido("Ingrese apellido (0 para regresar): ", sc, "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$", 
                                          " Apellido inválido. Solo letras y espacios.");
        if (apellido.equals("0")) return;

        String correo = leerCorreoUnico();
        if (correo.equals("0")) return;

        int edad = leerEnteroValido("Ingresar edad (0 para regresar): ", sc, " Edad inválida. Ingrese solo números positivos.");
        if (edad == 0) return;

        String estadoCivil = leerEstadoCivil();
        if (estadoCivil.equals("0")) return;

        Estudiante estudiante = new Estudiante(nombre, apellido, correo, edad, estadoCivil);

        try {
            service.insertar(estudiante);
            System.out.println(" Estudiante agregado con éxito.");
        } catch (SQLException e) {
            System.out.println("? Error: " + e.getMessage());
        }
    }

    private static void actualizarEstudiante() {
        System.out.println("\n--- Actualizar Estudiante ---");

        String correo = leerTextoValido(" Ingrese el correo del estudiante a actualizar (0 para regresar): ", sc, ".+@.+\\..+", 
                                        " Formato de correo inválido.");
        if (correo.equals("0")) return;

        try {
            Estudiante estudiante = service.buscarPorCorreo(correo);
            if (estudiante == null) {
                System.out.println(" No existe un estudiante con ese correo.");
                return;
            }

            String nombre = leerTextoValido(" Nuevo nombre (0 para regresar): ", sc, "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$", 
                                            " Nombre inválido.");
            if (nombre.equals("0")) return;

            String apellido = leerTextoValido(" Nuevo apellido (0 para regresar): ", sc, "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$", 
                                              " Apellido inválido.");
            if (apellido.equals("0")) return;

            int edad = leerEnteroValido(" Nueva edad (0 para regresar): ", sc, "⚠️ Edad inválida.");
            if (edad == 0) return;

            String estadoCivil = leerEstadoCivil();
            if (estadoCivil.equals("0")) return;

            estudiante.setNombre(nombre);
            estudiante.setApellido(apellido);
            estudiante.setEdad(edad);
            estudiante.setEstadoCivil(estadoCivil);

            service.actualizar(estudiante);
            System.out.println(" Estudiante actualizado con éxito.");

        } catch (SQLException e) {
            System.out.println("? Error: " + e.getMessage());
        }
    }

    private static void eliminarEstudiante() {
        System.out.println("\n--- Eliminar Estudiante ---");
        String correo = leerTextoValido("Ingrese el correo del estudiante a eliminar (0 para regresar): ", sc, ".+@.+\\..+", 
                                        "Correo inválido.");
        if (correo.equals("0")) return;

        try {
            service.eliminar(correo);
            System.out.println(" Estudiante eliminado con éxito.");
        } catch (SQLException e) {
            System.out.println("? Error: " + e.getMessage());
        }
    }

    private static void listarEstudiantes() {
        System.out.println("\n--- Lista de Estudiantes ---");
        try {
            List<Estudiante> estudiantes = service.listar();
            if (estudiantes.isEmpty()) {
                System.out.println(" No hay estudiantes registrados.");
            } else {
                estudiantes.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("? Error: " + e.getMessage());
        }
    }

    private static void buscarPorCorreo() {
        System.out.println("\n--- Buscar Estudiante por Correo ---");
        String correo = leerTextoValido("Ingrese el correo a buscar (0 para regresar): ", sc, ".+@.+\\..+", 
                                        " Correo inválido.");
        if (correo.equals("0")) return;

        try {
            Estudiante estudiante = service.buscarPorCorreo(correo);
            if (estudiante == null) {
                System.out.println(" No existe un estudiante con ese correo.");
            } else {
                System.out.println(estudiante);
            }
        } catch (SQLException e) {
            System.out.println("? Error: " + e.getMessage());
        }
    }

 

    private static String leerTextoValido(String mensaje, Scanner sc, String regex, String errorMsg) {
        String input;
        while (true) {
            System.out.print(mensaje);
            input = sc.nextLine().trim();
            if (input.equals("0")) return "0"; // volver al menú
            if (input.matches(regex)) return input;
            System.out.println(errorMsg);
        }
    }

    private static int leerEnteroValido(String mensaje, Scanner sc, String errorMsg) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (input.equals("0")) return 0; // volver al menú
            try {
                int numero = Integer.parseInt(input);
                if (numero > 0) return numero;
            } catch (NumberFormatException ignored) {}
            System.out.println(errorMsg);
        }
    }

    private static String leerCorreoUnico() {
        while (true) {
            String correo = leerTextoValido("Ingrese correo electrónico (0 para regresar): ", sc, ".+@.+\\..+", 
                                            " Correo inválido.");
            if (correo.equals("0")) return "0";
            try {
                if (service.buscarPorCorreo(correo) != null) {
                    System.out.println(" El correo ya existe. Ingrese otro.");
                } else {
                    return correo;
                }
            } catch (SQLException e) {
                System.out.println("? Error verificando correo: " + e.getMessage());
            }
        }
    }

    private static String leerEstadoCivil() {
        String[] opciones = {"SOLTERO", "CASADO", "VIUDO", "UNION_LIBRE", "DIVORCIADO"};
        while (true) {
            System.out.println("Seleccione estado civil (0 para regresar): ");
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            String input = sc.nextLine().trim();
            if (input.equals("0")) return "0";
            try {
                int opcion = Integer.parseInt(input);
                if (opcion >= 1 && opcion <= opciones.length) {
                    return opciones[opcion - 1];
                }
            } catch (NumberFormatException ignored) {}
            System.out.println(" Selección inválida. Ingrese un número entre 1 y " + opciones.length + ".");
        }
    }
}
