
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
public class Starbucks {
    static final String ADMIN_USUARIO = "admin@starbucks.com";
    static final String ADMIN_CONTRASENA = "admin1234";
    static ArrayList<String> bebidasNombres = new ArrayList<>();
    static ArrayList<Double> bebidasPrecios = new ArrayList<>();

    static ArrayList<String> alimentosNombres = new ArrayList<>();
    static ArrayList<Double> alimentosPrecios = new ArrayList<>();
    static ArrayList<String> clientesCorreos = new ArrayList<>();
    static ArrayList<String> clientesContrasenas = new ArrayList<>();
    static ArrayList<String> clientesNombres = new ArrayList<>();
    static ArrayList<String> clientesDNI = new ArrayList<>();
    static ArrayList<String> clientesTelefonos = new ArrayList<>();
    static ArrayList<String> clientesFechaNacimiento = new ArrayList<>();
    static ArrayList<Integer> clientesCompras = new ArrayList<>();
    static ArrayList<Double> clientesTotalGastado = new ArrayList<>();
    static ArrayList<String> carritoNombres = new ArrayList<>();
    static ArrayList<Integer> cantidades = new ArrayList<>();
    static ArrayList<Double> preciosUnitarios = new ArrayList<>();
    static ArrayList<String> productosNombres = new ArrayList<>();
    static ArrayList<Double> productosPrecios = new ArrayList<>();
    static int indiceClienteActivo = -1;
    static String nombreSesion = "";
    static String dniSesion = "";
    static String telefonoSesion = "";
    static String departamentoSesion = "";
    static boolean bebidaCumpleanosGratis = false;
    static boolean bebidaClienteFrecuente = false;
    public static void main(String[] args) {
        cargarClientes();
        Scanner sc = new Scanner(System.in);

        ejecutorSistema(sc);

        sc.close();
    }
    public static void ejecutorSistema (Scanner sc) {
        inicializarProductos();
        int opcion;
        System.out.println("=================================");
        System.out.println("     |   STARBUCKS PERÚ   |      ");
        System.out.println("=================================");
        System.out.println("1. Iniciar Sesión/Registro");
        System.out.println("2. Invitado");
        System.out.println("3. Panel de Administrador");
        System.out.println("4. Finalizar");
        System.out.print("Seleccione una opción: ");
        opcion = sc.nextInt();
        sc.nextLine();
        switch (opcion) {
            case 1:
                iniciarSesion(sc);
                break;
            case 2:
                invitado();
                break;
            case 3:
                panelAdministrador(sc);
                ejecutorSistema(sc);
                return;
            case 4:
                System.out.println("☕ Gracias por visitar Starbucks Perú");
                return;
            default:
                System.out.println("❌ Opción inválida");
                return;
        }
        verificarCumpleanos();
        otorgarBebidaCumpleanos();
        otorgarBebidaClienteFrecuente();
        int opselc;
        do {
            menuStarbucks();
            System.out.print("Seleccione una opción: ");
            opselc = sc.nextInt();
            sc.nextLine();
            if (opselc == 1) {
                menuBebidas(sc);
            } else if (opselc == 2) {
                menuAlimentos(sc);
            } else if (opselc == 3) {
                verCarrito();
            } else if (opselc == 5) {
                mostrarProductosDinamicos(sc);
            } else if (opselc != 4) {
                System.out.println("❌ Opción inválida");
            }
            if (opselc != 4) {
                System.out.println("¿Qué desea hacer?");
                System.out.println("1. Volver al Menú Principal");
                System.out.println("2. Ver carrito");
                System.out.println("3. Vaciar carrito");
                System.out.println("4. Proceder a pagar");
                System.out.print("Seleccione una opción: ");
                opselc = sc.nextInt();
                sc.nextLine();
                if (opselc == 2) {
                    verCarrito();
                    opselc = 1;
                } else if (opselc == 3) {
                    vaciarCarrito();
                    opselc = 1;
                }
            }
        } while (opselc != 4);
        double totalGeneral = calcularTotalCarrito();
        if (totalGeneral > 0) {
            verCarrito();
            totalGeneral = aplicarDescuento(totalGeneral);
            String nombre;
            String dni;
            String telefono;
            if (indiceClienteActivo != -1) {
                nombre = clientesNombres.get(indiceClienteActivo);
                dni = clientesDNI.get(indiceClienteActivo);
                telefono = clientesTelefonos.get(indiceClienteActivo);
                System.out.println("=================================");
                System.out.println("DATOS DEL CLIENTE RECUPERADOS");
                System.out.println("=================================");
                System.out.println("Nombre   : " + nombre);
                System.out.println("DNI      : " + dni);
                System.out.println("Telefono : " + telefono);
            } else {
                System.out.println("=== REGISTRO DE DATOS PARA COMPRA ===");
                System.out.print("Ingrese su nombre: ");
                nombre = sc.nextLine();
                System.out.print("Ingrese su DNI: ");
                dni = sc.nextLine();
                telefono = pedirTelefono(sc);
            }
            String depto;
            if (indiceClienteActivo != -1) {
                if (departamentoSesion.equals("")) {
                    depto = elegirDepartamento(sc);
                    departamentoSesion = depto;
                } else {
                    depto = departamentoSesion;
                }
            } else {
                depto = elegirDepartamento(sc);
            }
            String tienda = elegirTienda(sc, depto);
            String metodoPago = elegirMetodoPago(sc, totalGeneral);
            if (indiceClienteActivo != -1) {
                int comprasActuales = clientesCompras.get(indiceClienteActivo);
                clientesCompras.set(indiceClienteActivo, comprasActuales + 1);
                double gastadoActual = clientesTotalGastado.get(indiceClienteActivo);
                clientesTotalGastado.set(indiceClienteActivo, gastadoActual + totalGeneral);
                actualizarArchivoClientes();
            }

            String correoParaBoleta = (indiceClienteActivo != -1)
                    ? clientesCorreos.get(indiceClienteActivo)
                    : "Usuario_Anonimo";
            animacionCarga();
            generarBoleta(nombre, dni, correoParaBoleta, telefono, depto, totalGeneral, metodoPago, tienda);
        } else {
            System.out.println("☕ No se realizaron compras. ¡Que tenga un buen día!");
        }
    }
    public static void inicializarProductos() {

        if (!productosNombres.isEmpty()) {
            return;
        }

        productosNombres.add("Café Americano");
        productosPrecios.add(10.50);

        productosNombres.add("Frappuccino de Manjar Blanco");
        productosPrecios.add(16.50);

        productosNombres.add("Muffin de Arándanos");
        productosPrecios.add(8.00);

        productosNombres.add("Croissant de Jamón y Queso");
        productosPrecios.add(11.00);
    }
    public static void mostrarProductosDinamicos(Scanner sc) {

        if (productosNombres.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.println("========= PRODUCTOS AGREGADOS =========");

        for (int i = 0; i < productosNombres.size(); i++) {
            System.out.println((i + 1) + ". "
                    + productosNombres.get(i)
                    + " - S/ "
                    + productosPrecios.get(i));
        }

        System.out.print("Seleccione producto: ");
        int opcion = sc.nextInt();

        if (opcion < 1 || opcion > productosNombres.size()) {
            System.out.println("❌ Opción inválida");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = sc.nextInt();

        agregarAlCarrito(
                productosNombres.get(opcion - 1),
                cantidad,
                productosPrecios.get(opcion - 1)
        );
    }

    public static void panelAdministrador(Scanner sc) {
        System.out.println("=================================");
        System.out.println("      PANEL DE ADMINISTRADOR     ");
        System.out.println("=================================");
        System.out.print("Ingrese usuario administrador: ");
        String usuarioIngresado = sc.nextLine();
        System.out.print("Ingrese contraseña: ");
        String contrasenaIngresada = sc.nextLine();
        if (!usuarioIngresado.equals(ADMIN_USUARIO) || !contrasenaIngresada.equals(ADMIN_CONTRASENA)) {
            System.out.println("❌ Credenciales de administrador incorrectas.");
            return;
        }
        System.out.println("✅ Acceso de administrador concedido.");
        int opAdmin;
        do {
            System.out.println("=========================================");
            System.out.println("   MENÚ ADMINISTRADOR - STARBUCKS PERÚ  ");
            System.out.println("=========================================");
            System.out.println("1. Ver lista de clientes registrados");
            System.out.println("2. Ver estadísticas de clientes");
            System.out.println("3. Añadir productos");
            System.out.println("4. Eliminar productos");
            System.out.println("5. Ver clientes frecuentes (3+ compras)");
            System.out.println("6. Salir del panel");
            System.out.print("Seleccione una opción: ");
            opAdmin = sc.nextInt();
            sc.nextLine();
            switch (opAdmin) {
                case 1:
                    adminVerClientes();
                    break;

                case 2:
                    adminEstadisticas();
                    break;

                case 3:
                    adminAñadirproductos(sc);
                    break;

                case 4:
                    adminEliminarproductos(sc);
                    break;

                case 5:
                    adminClientesFrecuentes();
                    break;

                case 6:
                    System.out.println("Saliendo del panel de administrador...");
                    return;

                default:
                    System.out.println("❌ Opción inválida");
            }
        } while (opAdmin != 6);
    }
    public static void adminVerClientes() {
        if (clientesCorreos.isEmpty()) {
            System.out.println("No hay clientes registrados aún.");
            return;
        }
        System.out.println("========= CLIENTES REGISTRADOS =========");
        for (int i = 0; i < clientesCorreos.size(); i++) {
            System.out.println("------------------------------------------");
            System.out.println(" Cliente #" + (i + 1));
            System.out.println(" Nombre    : " + clientesNombres.get(i));
            System.out.println(" Correo    : " + clientesCorreos.get(i));
            System.out.println(" DNI       : " + clientesDNI.get(i));
            System.out.println(" Teléfono  : " + clientesTelefonos.get(i));
            System.out.println(" Cumpleaños: " + clientesFechaNacimiento.get(i));
            System.out.println(" Compras   : " + clientesCompras.get(i));
            System.out.println(" Total S/  : " + clientesTotalGastado.get(i));
        }
        System.out.println("==========================================");
    }
    public static void adminEstadisticas() {
        System.out.println("========= ESTADÍSTICAS =========");
        System.out.println("Total clientes registrados: " + clientesCorreos.size());
        double totalRecaudado = 0;
        for (int i = 0; i < clientesTotalGastado.size(); i++) {
            totalRecaudado += clientesTotalGastado.get(i);
        }
        System.out.println("Total recaudado en sistema: S/ " + (Math.round(totalRecaudado * 100) / 100.0));
        int frecuentes = 0;
        for (int i = 0; i < clientesCompras.size(); i++) {
            if (clientesCompras.get(i) >= 3) {
                frecuentes++;
            }
        }
        System.out.println("Clientes frecuentes (3+ compras): " + frecuentes);
    }
    public static void adminAñadirproductos(Scanner sc) {

        System.out.println("1. Bebida");
        System.out.println("2. Alimento");

        int categoria = sc.nextInt();
        sc.nextLine();

        System.out.print("Ingrese nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Ingrese precio: ");
        double precio = sc.nextDouble();
        sc.nextLine();

        if (categoria == 1) {

            productosNombres.add(nombre);
            productosPrecios.add(precio);

            System.out.println("✅ Bebida agregada.");

        } else if (categoria == 2) {

            productosNombres.add(nombre);
            productosPrecios.add(precio);

            System.out.println("✅ Alimento agregado.");

        } else {

            System.out.println("❌ Categoria invalida.");

        }
    }
    public static void adminEliminarproductos(Scanner sc) {

        if (productosNombres.isEmpty()) {

            System.out.println("❌ No existen productos registrados para eliminar.");
            return;

        }

        System.out.println("=================================");
        System.out.println("      PRODUCTOS REGISTRADOS");
        System.out.println("=================================");

        for (int i = 0; i < productosNombres.size(); i++) {

            System.out.println((i + 1) + ". " +
                    productosNombres.get(i) +
                    " - S/ " +
                    productosPrecios.get(i));

        }

        System.out.print("Seleccione el numero del producto a eliminar: ");

        int opcion = sc.nextInt();
        sc.nextLine();

        if (opcion < 1 || opcion > productosNombres.size()) {

            System.out.println("❌ Opcion invalida.");
            return;

        }

        String productoEliminado =
                productosNombres.get(opcion - 1);

        productosNombres.remove(opcion - 1);
        productosPrecios.remove(opcion - 1);

        System.out.println("✅ Producto eliminado correctamente.");
        System.out.println("Producto eliminado: " + productoEliminado);

    }
    public static void adminClientesFrecuentes() {
        System.out.println("========= CLIENTES FRECUENTES (3+ compras) =========");
        boolean hayFrecuentes = false;
        for (int i = 0; i < clientesCompras.size(); i++) {
            if (clientesCompras.get(i) >= 3) {
                System.out.println(" Nombre  : " + clientesNombres.get(i));
                System.out.println(" Correo  : " + clientesCorreos.get(i));
                System.out.println(" Compras : " + clientesCompras.get(i));
                System.out.println("----------------------------------------------");
                hayFrecuentes = true;
            }
        }
        if (!hayFrecuentes) {
            System.out.println("No hay clientes frecuentes aún.");
        }
    }
    public static void iniciarSesion(Scanner sc) {
        System.out.println("=================================");
        System.out.println("  1. ¿Ya tienes cuenta? Inicia sesión");
        System.out.println("  2. Crear nueva cuenta");
        System.out.println("Seleccione: ");
        int opLogin = sc.nextInt();
        sc.nextLine();
        if (opLogin == 2) {
            registrarCliente(sc);
        } else if (opLogin == 1) {
            loginCliente(sc);
        } else {
            System.out.println("❌ Opción inválida");
        }
    }
    public static void registrarCliente(Scanner sc) {
        System.out.println("=================================");
        System.out.println("       CREAR UNA CUENTA          ");
        System.out.println("=================================");
        String correoRegistrado;
        do {
            System.out.println("Registre su correo Gmail: ");
            correoRegistrado = sc.nextLine();
            if (!correoRegistrado.endsWith("@gmail.com")) {
                System.out.println("❌ El correo debe terminar en @gmail.com");
            }
        } while (!correoRegistrado.endsWith("@gmail.com"));
        for (int i = 0; i < clientesCorreos.size(); i++) {
            if (clientesCorreos.get(i).equals(correoRegistrado)) {
                System.out.println("❌ Ese correo ya está registrado. Inicie sesión.");
                loginCliente(sc);
                return;
            }
        }
        String contrasenaRegistrada;
        do {
            System.out.println("Cree su contraseña (mínimo 8 caracteres): ");
            contrasenaRegistrada = sc.nextLine();
            if (contrasenaRegistrada.length() < 8) {
                System.out.println("❌ La contraseña es muy corta");
            }
        } while (contrasenaRegistrada.length() < 8);
        System.out.println("Ingrese su nombre completo: ");
        String nombreReg = sc.nextLine();
        System.out.println("Ingrese su DNI: ");
        String dniReg = sc.nextLine();
        String telefonoReg = pedirTelefono(sc);
        System.out.println("Ingrese su fecha de nacimiento (DD/MM/YYYY) : ");
        String fechaNac = sc.nextLine();
        if (fechaNac.length() >= 5) {
            fechaNac = fechaNac.substring(0, 5);
        }
        clientesCorreos.add(correoRegistrado);
        clientesContrasenas.add(contrasenaRegistrada);
        clientesNombres.add(nombreReg);
        clientesDNI.add(dniReg);
        clientesTelefonos.add(telefonoReg);
        clientesFechaNacimiento.add(fechaNac);
        clientesCompras.add(0);
        clientesTotalGastado.add(0.0);
        guardarClienteEnArchivo(
                correoRegistrado,
                contrasenaRegistrada,
                nombreReg,
                dniReg,
                telefonoReg,
                fechaNac
        );
        indiceClienteActivo = clientesCorreos.size() - 1;
        System.out.println("✅ Cuenta creada correctamente.");
        System.out.println("☕ Bienvenido a Starbucks Perú, " + nombreReg + "!");
    }
    public static void guardarClienteEnArchivo(
            String correo,
            String contrasena,
            String nombre,
            String dni,
            String telefono,
            String fechaNacimiento) {
        try {
            FileWriter escritor = new FileWriter("clientes.txt", true);
            escritor.write(
                    correo + "|" +
                            contrasena + "|" +
                            nombre + "|" +
                            dni + "|" +
                            telefono + "|" +
                            fechaNacimiento + "|" +
                            0 + "|" +
                            0.0 + "\n"
            );
            escritor.close();
        } catch (IOException e) {
            System.out.println("Error al guardar cliente.");
        }
    }
    public static void loginCliente(Scanner sc) {
        System.out.println("=================================");
        System.out.println("         INICIAR SESIÓN          ");
        System.out.println("=================================");
        boolean accesoConcedido = false;
        do {
            System.out.println("Ingrese su correo Gmail: ");
            String correoLogin = sc.nextLine();
            System.out.println("Ingrese su contraseña: ");
            String contrasenaLogin = sc.nextLine();
            boolean encontrado = false;
            for (int i = 0; i < clientesCorreos.size(); i++) {
                if (clientesCorreos.get(i).equals(correoLogin) &&
                        clientesContrasenas.get(i).equals(contrasenaLogin)) {
                    System.out.println("✅ ¡Inicio de sesión exitoso!");
                    System.out.println("☕ Bienvenido de vuelta, " + clientesNombres.get(i) + "!");
                    indiceClienteActivo = i;
                    accesoConcedido = true;
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("❌ Correo o contraseña incorrectos. Intente de nuevo.\n");
            }
        } while (!accesoConcedido);
    }
    public static void invitado() {
        System.out.println("☕ Bienvenido invitado a Starbucks Perú");
        indiceClienteActivo = -1;
    }
    public static void verificarCumpleanos() {
        if (indiceClienteActivo == -1) return;
        LocalDateTime ahora = LocalDateTime.now();
        String diaHoy = ahora.format(DateTimeFormatter.ofPattern("dd"));
        String mesHoy = ahora.format(DateTimeFormatter.ofPattern("MM"));
        String hoyDDMM = diaHoy + "/" + mesHoy;
        String fechaNacCliente = clientesFechaNacimiento.get(indiceClienteActivo);
        int comprasCliente = clientesCompras.get(indiceClienteActivo);
        if (fechaNacCliente.equals(hoyDDMM)) {
            bebidaCumpleanosGratis = true;
            System.out.println("==============================================");
            System.out.println("  🎂 ¡FELIZ CUMPLEAÑOS, " + clientesNombres.get(indiceClienteActivo).toUpperCase() + "! 🎂");
            System.out.println("==============================================");
            if (comprasCliente >= 3) {
                System.out.println("🎁 Por ser cliente frecuente y cumplir años hoy,");
                System.out.println("   Starbucks te regala una bebida a elección.");
            } else {
                System.out.println("🎁 Starbucks te regala una bebida a elección.");
            }
            System.out.println("✅ Bebida gratuita agregada al pedido.");
            System.out.println("==============================================");
        }
        else if (comprasCliente >= 3) {
            bebidaClienteFrecuente = true;
            System.out.println("⭐ ¡Hola cliente frecuente!");
            System.out.println("🎁 Starbucks te obsequia una bebida sorpresa.");
        }
    }
    public static void otorgarBebidaCumpleanos() {
        if (!bebidaCumpleanosGratis) {
            return;
        }
        carritoNombres.add("Bebida Gratis de Cumpleaños");
        cantidades.add(1);
        preciosUnitarios.add(0.0);
        System.out.println("🎁 Se agregó una bebida gratis de cumpleaños al carrito.");
        bebidaCumpleanosGratis = false;
    }
    public static void otorgarBebidaClienteFrecuente() {
        if (!bebidaClienteFrecuente) {
            return;
        }
        ArrayList<String> bebidas = new ArrayList<>();
        bebidas.add("Latte");
        bebidas.add("Americano");
        bebidas.add("Mocha Cafe");
        bebidas.add("Flat White");
        bebidas.add("Caramel Macchiato");
        int aleatorio = (int)(Math.random() * bebidas.size());
        String bebidaSeleccionada = bebidas.get(aleatorio);
        carritoNombres.add(bebidaSeleccionada + " GRATIS");
        cantidades.add(1);
        preciosUnitarios.add(0.0);
        System.out.println("☕ Bebida gratuita agregada por cliente frecuente:");
        System.out.println("🎁 " + bebidaSeleccionada);
        bebidaClienteFrecuente = false;
    }
    public static void menuStarbucks() {
        System.out.println("========== C A T E G O R I A S ==========");
        System.out.println("1. Bebidas");
        System.out.println("2. Alimentos");
        System.out.println("3. Ver carrito 🛒");
        System.out.println("4. Proceder a pagar");
        System.out.println("5. Productos nuevos");
        System.out.println("Seleccione una opción: ");
    }
    public static void agregarAlCarrito(String nombre, int cantidad, double precioUnitario) {
        // Verificar si el producto ya está en el carrito
        boolean yaExiste = false;
        for (int i = 0; i < carritoNombres.size(); i++) {
            if (carritoNombres.get(i).equals(nombre)) {
                cantidades.set(i, cantidades.get(i) + cantidad);
                yaExiste = true;
                break;
            }
        }
        if (!yaExiste) {
            carritoNombres.add(nombre);
            cantidades.add(cantidad);
            preciosUnitarios.add(precioUnitario);
        }
        System.out.println("✅ Agregado al carrito: " + cantidad + "x " + nombre +
                " | Subtotal: S/ " + (Math.round(precioUnitario * cantidad * 100) / 100.0));
    }
    public static void verCarrito() {
        if (carritoNombres.isEmpty()) {
            System.out.println("🛒 El carrito está vacío.");
            return;
        }
        System.out.println("========= 🛒 TU CARRITO =========");
        double totalCarrito = 0;
        for (int i = 0; i < carritoNombres.size(); i++) {
            double subtotalItem = preciosUnitarios.get(i) * cantidades.get(i);
            System.out.println((i + 1) + ". " + carritoNombres.get(i) +
                    " x" + cantidades.get(i) +
                    "  ->  S/ " + (Math.round(subtotalItem * 100) / 100.0));
            totalCarrito += subtotalItem;
        }
        System.out.println("----------------------------------");
        System.out.println("TOTAL: S/ " + (Math.round(totalCarrito * 100) / 100.0));
        System.out.println("==================================");
    }
    public static void vaciarCarrito() {
        carritoNombres.clear();
        cantidades.clear();
        preciosUnitarios.clear();
        System.out.println("🗑️ Carrito vaciado.");
    }
    public static double calcularTotalCarrito() {
        double total = 0;
        for (int i = 0; i < carritoNombres.size(); i++) {
            total += preciosUnitarios.get(i) * cantidades.get(i);
        }
        return Math.round(total * 100) / 100.0;
    }
    public static double aplicarDescuento(double total) {
        System.out.println("==========================================");
        System.out.println("           RESUMEN DE DESCUENTOS         ");
        System.out.println("==========================================");
        double totalConDescuento = total;
        if (total > 119.9) {
            double descuento = Math.round(total * 0.10 * 100) / 100.0;
            totalConDescuento = Math.round((total - descuento) * 100) / 100.0;
            System.out.println("🎉 ¡Compra mayor a S/119.90!");
            System.out.println("   Descuento del 10%: -S/ " + descuento);
            System.out.println("   Total con descuento: S/ " + totalConDescuento);
        } else {
            System.out.println("   Total sin descuento: S/ " + total);
            System.out.println("   (Compras mayores a S/119.90 tienen 10% de descuento)");
        }
        if (indiceClienteActivo != -1 && clientesCompras.get(indiceClienteActivo) >= 3) {
            LocalDateTime ahora = LocalDateTime.now();
            String diaHoy = ahora.format(DateTimeFormatter.ofPattern("dd"));
            String mesHoy = ahora.format(DateTimeFormatter.ofPattern("MM"));
            String hoyDDMM = diaHoy + "/" + mesHoy;
            String fechaNacCliente = clientesFechaNacimiento.get(indiceClienteActivo);
            if (!fechaNacCliente.equals(hoyDDMM)) {
                double descuentoFrecuente = Math.round(totalConDescuento * 0.05 * 100) / 100.0;
                totalConDescuento = Math.round((totalConDescuento - descuentoFrecuente) * 100) / 100.0;
                System.out.println("⭐ Descuento cliente frecuente (5%): -S/ " + descuentoFrecuente);
                System.out.println("   Total final: S/ " + totalConDescuento);
            }
        }
        System.out.println("==========================================");
        return totalConDescuento;
    }
    public static String pedirTelefono(Scanner sc) {
        String telefono;
        do {
            System.out.println("Ingrese su número de teléfono (debe empezar por 9): ");
            telefono = sc.nextLine();
            if (!telefono.startsWith("9")) {
                System.out.println("❌ El número de teléfono debe empezar por 9. Intente nuevamente.");
            } else if (telefono.length() != 9) {
                System.out.println("❌ El número de teléfono debe tener 9 dígitos.");
                telefono = "";
            }
        } while (!telefono.startsWith("9") || telefono.length() != 9);
        return telefono;
    }
    public static void menuBebidas(Scanner sc) {
        int opcion;
        do {
            System.out.println("========== B E B I D A S ==========");
            System.out.println("1. Frappuccinos");
            System.out.println("2. Cafés Calientes");
            System.out.println("3. Bebidas extras agregadas");
            System.out.println("4. Terminar selección de bebidas");
            System.out.println("Seleccione una opción: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    frappuccino(sc);
                    break;
                case 2:
                    CafeCaliente(sc);
                    break;
                case 3:
                    mostrarBebidasAdministrador(sc);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 4);
    }
    public static void frappuccino(Scanner scanner) {
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("Black & White Mocha Frappuccino");
        nombres.add("Cookies & Cream Creme Frappuccino");
        nombres.add("Ultimate Caramel Frappuccino");
        nombres.add("Mocha Frappuccino");
        nombres.add("Chocolucuma Frappuccino");
        nombres.add("Caramel Frappuccino");
        nombres.add("Triple Mocha Frappuccino");
        nombres.add("Algarrobina Frappuccino");
        nombres.add("Chocolate Creme Frappuccino");
        nombres.add("Lucuma Creme Frappuccino");
        ArrayList<Double> precios = new ArrayList<>();
        precios.add(17.50);
        precios.add(17.50);
        precios.add(17.00);
        precios.add(16.00);
        precios.add(18.00);
        precios.add(16.00);
        precios.add(17.50);
        precios.add(16.50);
        precios.add(16.00);
        precios.add(17.50);
        System.out.println("---------- F R A P P U C C I N O S ----------");
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println((i + 1) + ": " + nombres.get(i) + " - S/" + precios.get(i));
        }
        System.out.println("==========================================");
        System.out.println("Ingrese opción: ");
        int eleccion = scanner.nextInt();
        int cantidad;
        if (eleccion >= 1 && eleccion <= nombres.size()) {
            System.out.println("Cantidad: ");
            cantidad = scanner.nextInt();
            agregarAlCarrito(nombres.get(eleccion - 1), cantidad, precios.get(eleccion - 1));
        } else {
            System.out.println("Opción fuera de rango");
        }
    }
    public static void CafeCaliente(Scanner sc) {
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("Flat White");
        nombres.add("Latte");
        nombres.add("Latte Macchiato");
        nombres.add("Caramel Macchiato");
        nombres.add("Mocha Cafe");
        nombres.add("Vainilla Latte");
        nombres.add("Skinny Vainilla Latte");
        nombres.add("Mocha Blanco Cafe");
        nombres.add("Algarrobina Latte");
        nombres.add("Americano");
        ArrayList<Double> precios = new ArrayList<>();
        precios.add(15.50);
        precios.add(15.00);
        precios.add(13.00);
        precios.add(15.00);
        precios.add(15.50);
        precios.add(14.50);
        precios.add(15.50);
        precios.add(15.50);
        precios.add(14.50);
        precios.add(16.00);
        System.out.println("---------- CAFÉS CALIENTES ----------");
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println((i + 1) + ": " + nombres.get(i) + " - S/" + precios.get(i));
        }
        System.out.println("Ingrese opción: ");
        int eleccion = sc.nextInt();
        int cantidad;

        if (eleccion >= 1 && eleccion <= nombres.size()) {
            System.out.println("Cantidad: ");
            cantidad = sc.nextInt();
            agregarAlCarrito(nombres.get(eleccion - 1), cantidad, precios.get(eleccion - 1));
        } else {
            System.out.println("Opción fuera de rango");
        }
    }
    public static void mostrarBebidasAdministrador(Scanner sc) {

        if (bebidasNombres.isEmpty()) {
            System.out.println("No hay bebidas agregadas.");
            return;
        }

        System.out.println("======= BEBIDAS AGREGADAS =======");

        for (int i = 0; i < bebidasNombres.size(); i++) {
            System.out.println((i + 1) + ". "
                    + bebidasNombres.get(i)
                    + " - S/ "
                    + bebidasPrecios.get(i));
        }

        System.out.print("Seleccione bebida: ");
        int opcion = sc.nextInt();

        if (opcion < 1 || opcion > bebidasNombres.size()) {
            System.out.println("❌ Opción inválida");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = sc.nextInt();

        agregarAlCarrito(
                bebidasNombres.get(opcion - 1),
                cantidad,
                bebidasPrecios.get(opcion - 1)
        );
    }
//------------------------------------------------jhosef
    public static void menuAlimentos(Scanner l) {
        int opcion;
        do {
            System.out.println("--- A L I M E N T O S----");
            System.out.println("1. Pastries");
            System.out.println("2. Sandwiches");
            System.out.println("3. Alimentos  extras agregados");
            System.out.println("4. Terminar selección de alimentos");
            System.out.println("Seleccione opcion: ");
            opcion = l.nextInt();

            switch (opcion) {
                case 1:
                    menuPastries(l);
                    break;
                case 2:
                    menuSandwiches(l);
                    break;
                case 3:
                    mostrarAlimentosAdministrador(l);
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opcion != 4);
    }

    public static void menuPastries(Scanner l) {
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("Galleta Rellena de Crema de Avellanas");
        nombres.add("Muffin de Naranja & Chocochips");
        nombres.add("Keke de Zanahoria");
        nombres.add("Galleta de Avena & Cranberry");
        nombres.add("Keke de Limon");
        nombres.add("Galleta de Chocochips");
        nombres.add("Muffin de Berries");
        nombres.add("Egg Bites (2 un)");
        nombres.add("Galleta Mom");
        nombres.add("Cake Pop Mom");
        ArrayList<Double> precios = new ArrayList<>();
        precios.add(11.00);
        precios.add(10.50);
        precios.add(10.50);
        precios.add(7.50);
        precios.add(10.50);
        precios.add(7.50);
        precios.add(10.50);
        precios.add(10.00);
        precios.add(7.00);
        precios.add(7.00);
        System.out.println("--------P A S T R I E S-------");
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println((i + 1) + ". " + nombres.get(i) + " - S/" + precios.get(i));
        }
        System.out.println("Seleccione producto: ");
        int opcion = l.nextInt();
        if (opcion < 1 || opcion > nombres.size()) {
            System.out.println("Opcion invalida");
            return;
        }
        System.out.println("Ingrese cantidad: ");
        int cantidad = l.nextInt();
        agregarAlCarrito(nombres.get(opcion - 1), cantidad, precios.get(opcion - 1));
    }
    public static void menuSandwiches(Scanner l) {
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("Sandwich Finas Hierbas");
        nombres.add("Croissant de Mantequilla");
        nombres.add("Sandwich Croissant Jamon del Pais & Queso");
        nombres.add("Sandwich Croissant Jamon Ingles & Queso");
        nombres.add("Sandwich Pavita & Queso");
        nombres.add("Sandwich Eggmont");
        nombres.add("Sandwich Pavita, Queso & Espinaca");
        nombres.add("Sandwich Brioche Campesino");
        nombres.add("Sandwich Panino Vesubio");
        nombres.add("Sandwich Chicken Panino");
        ArrayList<Double> precios = new ArrayList<>();
        precios.add(17.50);
        precios.add(7.50);
        precios.add(16.50);
        precios.add(16.50);
        precios.add(14.50);
        precios.add(13.50);
        precios.add(15.00);
        precios.add(16.00);
        precios.add(13.50);
        precios.add(14.50);
        System.out.println("========= S A N D W I C H E S =========");
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println((i + 1) + ". " + nombres.get(i) + " - S/" + precios.get(i));
        }
        System.out.println("Seleccione producto: ");
        int opcion = l.nextInt();
        if (opcion < 1 || opcion > nombres.size()) {
            System.out.println("Opcion invalida");
            return;
        }
        System.out.println("Ingrese cantidad: ");
        int cantidad = l.nextInt();
        agregarAlCarrito(nombres.get(opcion - 1), cantidad, precios.get(opcion - 1));
    }
    public static void mostrarAlimentosAdministrador(Scanner sc) {

        if (alimentosNombres.isEmpty()) {
            System.out.println("No hay alimentos agregados.");
            return;
        }

        System.out.println("======= ALIMENTOS AGREGADOS =======");

        for (int i = 0; i < alimentosNombres.size(); i++) {
            System.out.println((i + 1) + ". "
                    + alimentosNombres.get(i)
                    + " - S/ "
                    + alimentosPrecios.get(i));
        }

        System.out.print("Seleccione alimento: ");
        int opcion = sc.nextInt();

        if (opcion < 1 || opcion > alimentosNombres.size()) {
            System.out.println("❌ Opción inválida");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = sc.nextInt();

        agregarAlCarrito(
                alimentosNombres.get(opcion - 1),
                cantidad,
                alimentosPrecios.get(opcion - 1)
        );
    }
    //Anderson_______________________________________________________________________________________________________
    public static String elegirDepartamento(Scanner sc) {
        ArrayList<String> departamentos = new ArrayList<>();
        departamentos.add("Lima");
        departamentos.add("Arequipa");
        departamentos.add("Cusco");
        departamentos.add("Trujillo");
        departamentos.add("Piura");
        departamentos.add("Puno");
        departamentos.add("Tacna");

        System.out.println("======= DEPARTAMENTO DE REGISTRO =======");
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println((i + 1) + ". " + departamentos.get(i));
        }
        System.out.println("Seleccione su departamento:");
        int opcion = sc.nextInt();
        sc.nextLine();
        if (opcion < 1 || opcion > departamentos.size()) {
            System.out.println("Opcion no valida.");
            System.exit(0);
        }
        String departamento = departamentos.get(opcion - 1);
        System.out.println("Departamento registrado: " + departamento);
        return departamento;
    }
    public static String elegirTienda(Scanner sc, String departamento) {
        String tienda = "";
        System.out.println("======= LUGAR DE RECOJO =======");
        System.out.println("Su departamento registrado es: " + departamento);
        System.out.println("Recoger en Starbucks " + departamento + "?");
        System.out.println("1. Si, recoger ahi");
        System.out.println("2. No, elegir otra tienda");
        System.out.println("Seleccione:");
        int opcion = sc.nextInt();
        sc.nextLine();
        if (opcion == 1) {
            tienda = "Starbucks " + departamento;
            System.out.println("Perfecto! Recoges en: " + tienda);
        } else if (opcion == 2) {
            tienda = elegirOtraTienda(sc);
        } else {
            System.out.println("Opcion no valida.");
            System.exit(0);
        }
        return tienda;
    }
    public static String elegirOtraTienda(Scanner sc) {
        ArrayList<String> tiendas = new ArrayList<>();
        tiendas.add("Starbucks Lima");
        tiendas.add("Starbucks Arequipa");
        tiendas.add("Starbucks Cusco");
        tiendas.add("Starbucks Trujillo");
        tiendas.add("Starbucks Piura");

        System.out.println("Seleccione el departamento donde recoger:");
        for (int i = 0; i < tiendas.size(); i++) {
            System.out.println((i + 1) + ". " + tiendas.get(i));
        }
        System.out.println("Seleccione:");
        int opcion = sc.nextInt();
        sc.nextLine();
        if (opcion < 1 || opcion > tiendas.size()) {
            System.out.println("Opcion no valida.");
            System.exit(0);
        }
        String tienda = tiendas.get(opcion - 1);
        System.out.println("Recoges en: " + tienda);
        return tienda;
    }
    public static String elegirMetodoPago(Scanner sc, double total) {
        System.out.println("======= MÉTODO DE PAGO =======");
        System.out.println("Monto total a pagar: S/ " + total);
        System.out.println("1. Tarjeta de crédito/débito");
        System.out.println("2. Yape");
        System.out.println("3. Plin");
        System.out.println("4. Prexpe");
        System.out.println("5. Lemon Pay");
        System.out.println("Seleccione método de pago: ");
        int opPago = sc.nextInt();
        sc.nextLine();
        String metodoPago = "";
        switch (opPago) {
            case 1:
                metodoPago = pagoTarjeta(sc, total);
                break;
            case 2:
                metodoPago = pagoYape(sc, total);
                break;
            case 3:
                metodoPago = pagoPlin(sc, total);
                break;
            case 4:
                metodoPago = pagoPrexpe(sc, total);
                break;
            case 5:
                metodoPago = pagoLemonPay(sc, total);
                break;
            default:
                System.out.println("❌ Opción inválida, se usará tarjeta por defecto.");
                metodoPago = pagoTarjeta(sc, total);
        }
        return metodoPago;
    }
    public static String pagoTarjeta(Scanner sc, double total) {
        String numeroTarjeta = "";
        String fechaVencimiento = "";
        String cvv = "";
        boolean pagoAprobado = false;
        System.out.println("Monto total a pagar con tarjeta: S/ " + total);
        do {
            System.out.println("Ingrese los 16 digitos de su tarjeta:");
            numeroTarjeta = sc.nextLine();
            System.out.println("Ingrese la fecha de caducidad (MM/AA):");
            fechaVencimiento = sc.nextLine();
            System.out.println("Ingrese el codigo de seguridad (CVV):");
            cvv = sc.nextLine();
            boolean tarjetaOK = numeroTarjeta.length() == 16;
            boolean cvvOK = cvv.length() == 3;
            boolean fechaOK = false;
            if (fechaVencimiento.length() == 5 && fechaVencimiento.contains("/")) {
                String partes[] = fechaVencimiento.split("/");
                int[] datosFecha = new int[partes.length];
                for (int i = 0; i < partes.length; i++) {
                    datosFecha[i] = Integer.parseInt(partes[i]);
                }
                int mes = datosFecha[0];
                int anio = datosFecha[1];
                int anioActual = 26;
                int mesActual = 6;
                if (mes >= 1 && mes <= 12) {
                    if (anio > anioActual) {
                        fechaOK = true;
                    } else if (anio == anioActual && mes >= mesActual) {
                        fechaOK = true;
                    }
                }
            }
            if (tarjetaOK && fechaOK && cvvOK) {
                pagoAprobado = true;
            } else {
                System.out.println("Operacion denegada. Verifique sus datos:");
                if (!tarjetaOK) System.out.println("La tarjeta debe tener 16 digitos.");
                if (!fechaOK) System.out.println("Targeta Caducada. Use MM/AA de una targeta vigente.");
                if (!cvvOK) System.out.println("El CVV debe tener 3 digitos.");
                System.out.println("Intente nuevamente.");
            }
        } while (!pagoAprobado);
        String numeroOculto = "************" + numeroTarjeta.substring(12);
        return "Tarjeta (" + numeroOculto + ")";
    }
    public static String pagoYape(Scanner sc, double total) {
        System.out.println("======= PAGO CON YAPE =======");
        System.out.println("Monto a pagar: S/ " + total);
        System.out.println("Número Yape de Starbucks: 999-888-777");
        System.out.println("Ingrese el número de celular Yape con el que pagó: ");
        String celularYape = sc.nextLine();
        if (!celularYape.startsWith("9") || celularYape.length() != 9) {
            System.out.println("⚠️ Número inválido, pero se registrará el intento.");
        }
        System.out.println("Ingrese el código de operación Yape (6 dígitos): ");
        String codigoYape = sc.nextLine();
        System.out.println("✅ Pago con Yape registrado. Código: " + codigoYape);
        return "Yape (" + celularYape + ")";
    }
    public static String pagoPlin(Scanner sc, double total) {
        System.out.println("======= PAGO CON PLIN =======");
        System.out.println("Monto a pagar: S/ " + total);
        System.out.println("Número Plin de Starbucks: 998-777-666");
        System.out.println("Ingrese el número de celular Plin con el que pagó: ");
        String celularPlin = sc.nextLine();
        System.out.println("Ingrese el código de operación Plin (6 dígitos): ");
        String codigoPlin = sc.nextLine();
        System.out.println("✅ Pago con Plin registrado. Código: " + codigoPlin);
        return "Plin (" + celularPlin + ")";
    }
    public static String pagoPrexpe(Scanner sc, double total) {
        System.out.println("======= PAGO CON PREXPE =======");
        System.out.println("Monto a pagar: S/ " + total);
        System.out.println("Ingrese su número de cuenta Prexpe: ");
        String cuentaPrexpe = sc.nextLine();
        System.out.println("Ingrese el código de confirmación Prexpe: ");
        String codigoPrexpe = sc.nextLine();
        System.out.println("✅ Pago con Prexpe registrado. Código: " + codigoPrexpe);
        return "Prexpe (" + cuentaPrexpe + ")";
    }
    public static String pagoLemonPay(Scanner sc, double total) {
        System.out.println("======= PAGO CON LEMON PAY =======");
        System.out.println("Monto a pagar: S/ " + total);
        System.out.println("Escanee el QR de Lemon Pay en tienda o ingrese su ID Lemon Pay: ");
        String idLemon = sc.nextLine();
        System.out.println("Ingrese el código de autorización Lemon Pay: ");
        String codigoLemon = sc.nextLine();
        System.out.println("✅ Pago con Lemon Pay registrado. ID: " + idLemon);
        return "Lemon Pay (ID: " + idLemon + ")";
    }
    public static void animacionCarga() {
        System.out.println();
        System.out.println("  Procesando su pago, por favor espere...");
        String[] pasos = {
                "  [■□□□□□□□□□]  10% - Verificando datos...",
                "  [■■■□□□□□□□]  30% - Validando método de pago...",
                "  [■■■■■□□□□□]  50% - Autorizando fondos...",
                "  [■■■■■■■□□□]  70% - Confirmando pedido...",
                "  [■■■■■■■■■□]  90% - Generando boleta...",
                "  [■■■■■■■■■■] 100% - ¡PAGO AUTORIZADO! ✅"
        };
        for (int i = 0; i < pasos.length; i++) {
            System.out.println(pasos[i]);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
    public static void generarBoleta(String nombre, String dni, String correo,
                                     String telefono, String departamento,
                                     double total, String metodoPago, String tienda) {
        double subtotal = total / 1.18;
        double igv = total - subtotal;
        String numeroBoleta = "B001-" + (100000 + (int)(Math.random() * 900000));
        String codigoValidacion = "SBX" + (100000 + (int)(Math.random() * 900000));
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        ArrayList<String> datosCliente = new ArrayList<>();
        datosCliente.add("Nombre       : " + nombre);
        datosCliente.add("DNI          : " + dni);
        datosCliente.add("Correo       : " + correo);
        datosCliente.add("Telefono     : " + telefono);
        datosCliente.add("Departamento : " + departamento);
        ArrayList<String> lineasBoleta = new ArrayList<>();
        lineasBoleta.add("==================================================");
        lineasBoleta.add("                 STARBUCKS COFFEE                 ");
        lineasBoleta.add("                 RUC: 20100070970                 ");
        lineasBoleta.add("      Av. Benavides 415, Miraflores - Lima        ");
        lineasBoleta.add("                 Tel: (01) 242-2600               ");
        lineasBoleta.add("==================================================");
        lineasBoleta.add("                 BOLETA DE VENTA                  ");
        lineasBoleta.add(" Boleta N°       : " + numeroBoleta);
        lineasBoleta.add(" Fecha           : " + fecha);
        lineasBoleta.add(" Hora            : " + hora);
        lineasBoleta.add(" Cod. Validacion : " + codigoValidacion);
        lineasBoleta.add("--------------------------------------------------");
        lineasBoleta.add(" DATOS DEL CLIENTE");
        lineasBoleta.add("--------------------------------------------------");
        for (int i = 0; i < datosCliente.size(); i++) {
            lineasBoleta.add(" " + datosCliente.get(i));
        }
        lineasBoleta.add("--------------------------------------------------");
        lineasBoleta.add(" DETALLE DE COMPRA");
        lineasBoleta.add("--------------------------------------------------");
        for (int i = 0; i < carritoNombres.size(); i++) {
            double subtotalItem = preciosUnitarios.get(i) * cantidades.get(i);
            String lineaProducto = " " + carritoNombres.get(i) +
                    " x" + cantidades.get(i) +
                    "  ->  S/ " + (Math.round(subtotalItem * 100) / 100.0);
            lineasBoleta.add(lineaProducto);
        }
        lineasBoleta.add("--------------------------------------------------");
        lineasBoleta.add(" Subtotal sin IGV : S/ " + (Math.round(subtotal * 100) / 100.0));
        lineasBoleta.add(" IGV (18%)        : S/ " + (Math.round(igv * 100) / 100.0));
        lineasBoleta.add(" TOTAL            : S/ " + total);
        lineasBoleta.add("--------------------------------------------------");
        lineasBoleta.add(" Metodo de pago   : " + metodoPago);
        lineasBoleta.add(" Tienda de recojo : " + tienda);
        lineasBoleta.add("--------------------------------------------------");
        lineasBoleta.add(" Esta boleta sirve como comprobante de compra.    ");
        lineasBoleta.add(" Conserve este documento para cualquier consulta. ");
        lineasBoleta.add("--------------------------------------------------");
        lineasBoleta.add("      Gracias por tu visita a Starbucks!          ");
        lineasBoleta.add("     Esperamos verte pronto. Have a nice day!     ");
        lineasBoleta.add("==================================================");
        for (int i = 0; i < lineasBoleta.size(); i++) {
            System.out.println(lineasBoleta.get(i));
        }
        String nombreArchivo = "Boleta_" + numeroBoleta + ".txt";
        try {
            FileWriter escritor = new FileWriter(nombreArchivo);
            for (int i = 0; i < lineasBoleta.size(); i++) {
                escritor.write(lineasBoleta.get(i) + "\n");
            }
            escritor.close();
            System.out.println("📄 Boleta guardada en: " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("⚠️ No se pudo guardar la boleta en archivo: " + e.getMessage());
        }
    }
    public static void cargarClientes() {
        try {
            BufferedReader lector =
                    new BufferedReader(new FileReader("clientes.txt"));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String datos[] = linea.split("\\|");
                clientesCorreos.add(datos[0]);
                clientesContrasenas.add(datos[1]);
                clientesNombres.add(datos[2]);
                clientesDNI.add(datos[3]);
                clientesTelefonos.add(datos[4]);
                clientesFechaNacimiento.add(datos[5]);
                clientesCompras.add(Integer.parseInt(datos[6]));
                clientesTotalGastado.add(Double.parseDouble(datos[7]));
            }
            lector.close();
        } catch (IOException e) {
        }
    }
    public static void actualizarArchivoClientes() {
        try {
            FileWriter escritor = new FileWriter("clientes.txt");
            for (int i = 0; i < clientesCorreos.size(); i++) {
                escritor.write(
                        clientesCorreos.get(i) + "|" +
                                clientesContrasenas.get(i) + "|" +
                                clientesNombres.get(i) + "|" +
                                clientesDNI.get(i) + "|" +
                                clientesTelefonos.get(i) + "|" +
                                clientesFechaNacimiento.get(i) + "|" +
                                clientesCompras.get(i) + "|" +
                                clientesTotalGastado.get(i) +
                                "\n"
                );
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Error al actualizar clientes.");
        }
    }
}