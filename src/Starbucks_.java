import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class Starbucks_ {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        double totalGeneral = 0;

        // === ARRAYLIST para el detalle de la compra
        ArrayList<String> detalleCompra = new ArrayList<>();
        String correoUsuario = "Usuario_Anonimo";
        System.out.println("=================================");
        System.out.println("     |   STARBUCKS PERÚ   |      ");
        System.out.println("=================================");
        System.out.println("1. Iniciar Sesión/Registro");
        System.out.println("2. Invitado");
        System.out.println("3. Finalizar");
        System.out.println("Seleccione una opción: ");
        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                iniciarSesion(sc);
                correoUsuario = "Registrado en Sistema";
                break;
            case 2:
                invitado();
                break;
            case 3:
                System.out.println("☕ Gracias por visitar Starbucks Perú");
                sc.close();
                return;
            default:
                System.out.println("❌ Opción inválida");
                sc.close();
                return;
        }
        int opselc;
        do {
            menuStarbucks();
            opselc = sc.nextInt();
            sc.nextLine();
            if (opselc == 1) {
                totalGeneral += menuBebidas(sc, detalleCompra);
            } else if (opselc == 2) {
                totalGeneral += menuAlimentos(sc, detalleCompra);
            } else if (opselc != 3) {
                System.out.println("❌ Opción inválida");
            }
            System.out.println("¿Desea agregar algo más? O presione 3 para proceder al pago.");
            System.out.println("1. Volver al Menú Principal");
            System.out.println("3. Proceder a pagar");
            opselc = sc.nextInt();
            sc.nextLine();
        } while (opselc != 3);
        if (totalGeneral > 0) {

            System.out.println("\n=== RESUMEN DE TU COMPRA ===");
            for (String item : detalleCompra) {
                System.out.println("→ " + item);
            }
            System.out.println("Total: S/ " + totalGeneral);
            System.out.println("============================\n");

            System.out.println("=== REGISTRO DE DATOS PARA COMPRA ===");
            System.out.println("Ingrese su nombre: ");
            String nombre = sc.nextLine();
            System.out.println("Ingrese su DNI: ");
            String dni = sc.nextLine();
            System.out.println("Ingrese su teléfono: ");
            String telefono = sc.nextLine();
            String depto = elegirDepartamento(sc);
            String tienda = elegirTienda(sc, depto);
            String tarjetaOculta = validarPagoTarjeta(sc, totalGeneral);
            generarBoleta(nombre, dni, correoUsuario, telefono, depto, totalGeneral, tarjetaOculta, tienda);
        } else {
            System.out.println("☕ No se realizaron compras. ¡Que tenga un buen día!");
        }
        sc.close();
    }
    // ====================  ====================
    public static void iniciarSesion(Scanner sc) {
        // (código completo de iniciarSesion sin cambios)
        String correoRegistrado;
        String contrasenaRegistrada;

        System.out.println("=================================");
        System.out.println("       1. CREAR UNA CUENTA       ");
        System.out.println("=================================");

        do {
            System.out.println("Registre su correo Gmail: ");
            correoRegistrado = sc.nextLine();

            if (!correoRegistrado.endsWith("@gmail.com")) {
                System.out.println("❌ El correo debe terminar en @gmail.com");
            }
        } while (!correoRegistrado.endsWith("@gmail.com"));

        do {
            System.out.println("Cree su contraseña (mínimo 8 caracteres): ");
            contrasenaRegistrada = sc.nextLine();

            if (contrasenaRegistrada.length() < 8) {
                System.out.println("❌ La contraseña es muy corta");
            }
        } while (contrasenaRegistrada.length() < 8);

        System.out.println("✅ Cuenta creada correctamente.");

        System.out.println("=================================");
        System.out.println("       2. INICIAR SESIÓN         ");
        System.out.println("=================================");
        System.out.println("Por favor, ingrese los datos de la cuenta que acaba de crear.");

        boolean accesoConcedido = false;
        do {
            System.out.println("Ingrese su correo Gmail: ");
            String correoLogin = sc.nextLine();

            System.out.println("Ingrese su contraseña: ");
            String contrasenaLogin = sc.nextLine();

            if (correoLogin.equals(correoRegistrado) && contrasenaLogin.equals(contrasenaRegistrada)) {
                System.out.println("✅ ¡Inicio de sesión exitoso!");
                System.out.println("☕ Bienvenido a Starbucks Perú");
                accesoConcedido = true;
            } else {
                System.out.println("❌ Los datos no coinciden con la cuenta registrada. Intente de nuevo.\n");
            }

        } while (!accesoConcedido);
    }

    public static void invitado() {
        System.out.println("☕ Bienvenido invitado a Starbucks Perú");
    }

    public static void menuStarbucks() {
        System.out.println("========== C A T E G O R I A S ==========");
        System.out.println("1. bebidas");
        System.out.println("2. alimentos");
        System.out.println("Seleccione una opción: ");
    }

    // ==================== modificado ====================

    public static double menuBebidas(Scanner sc, ArrayList<String> detalleCompra) {
        double totalGeneral = 0;
        int opcion;
        do {
            System.out.println("========== B E B I D A S ==========");
            System.out.println("1. Frappuccinos");
            System.out.println("2. Cafés Calientes");
            System.out.println("3. Terminar selección de bebidas");
            System.out.println("Seleccione una opción: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    totalGeneral += frappuccino(sc, detalleCompra);
                    break;
                case 2:
                    totalGeneral += CafeCaliente(sc, detalleCompra);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 3);
        return totalGeneral;
    }

    public static double frappuccino(Scanner scanner, ArrayList<String> detalleCompra) {
        double frappuccinototal = 0;
        int eleccion;
        int cantidad;
        System.out.println("---------- F R A P P U C C I N O S ----------");
        System.out.println("1: Black & White Mocha Frappuccino - s/17.50");
        System.out.println("2: Cookies & Cream Creme Frappuccino - s/17.50");
        System.out.println("3: Ultimate Caramel Frappuccino - s/17.00");
        System.out.println("4: Mocha Frappuccino - s/16.00");
        System.out.println("5: Chocolúcuma Frappuccino - s/18.00");
        System.out.println("6: Caramel Frappuccino - s/16.00");
        System.out.println("7: Triple Mocha Frappuccino - s/17.50");
        System.out.println("8: Algarrobina Frappuccino - s/16.50");
        System.out.println("9: Chocolate Creme Frappuccino - s/16.00");
        System.out.println("10: Lúcuma Creme Frappuccino - s/17.50");
        System.out.println("==========================================");
        System.out.println("Ingrese opción: ");
        eleccion = scanner.nextInt();
        if (eleccion >= 1 && eleccion <= 10) {
            System.out.println("Cantidad: ");
            cantidad = scanner.nextInt();
            switch (eleccion) {
                case 1: frappuccinototal = 17.50 * cantidad; break;
                case 2: frappuccinototal = 17.50 * cantidad; break;
                case 3: frappuccinototal = 17.00 * cantidad; break;
                case 4: frappuccinototal = 16.00 * cantidad; break;
                case 5: frappuccinototal = 18.00 * cantidad; break;
                case 6: frappuccinototal = 16.00 * cantidad; break;
                case 7: frappuccinototal = 17.50 * cantidad; break;
                case 8: frappuccinototal = 16.50 * cantidad; break;
                case 9: frappuccinototal = 16.00 * cantidad; break;
                case 10: frappuccinototal = 17.50 * cantidad; break;
            }
            System.out.println("Precio frappuccinos: S/ " + frappuccinototal);
            // === guarda en arraylist ===
            String nombre = getNombreFrappuccino(eleccion);
            detalleCompra.add(nombre + " x" + cantidad + " = S/" + frappuccinototal);
        } else {
            System.out.println("Opción fuera de rango");
        }
        return frappuccinototal;
    }
    public static double CafeCaliente(Scanner sc, ArrayList<String> detalleCompra) {
        double CafeCalientetotal = 0;
        int eleccion;
        int cantidad;
        System.out.println("---------- CAFÉS CALIENTES ----------");
        System.out.println("1: Flat White - s/15.50");
        System.out.println("2: Latte - s/15.00");
        System.out.println("3: Latte Macchiato - s/13.00");
        System.out.println("4: Caramel Macchiato - s/15.00");
        System.out.println("5: Mocha Café - s/15.50");
        System.out.println("6: Vainilla Latte - s/14.50");
        System.out.println("7: Skinny Vainilla Latte - s/15.50");
        System.out.println("8: Mocha Blanco Café - s/15.50");
        System.out.println("9: Algarrobina Latte - s/14.50");
        System.out.println("10: Americano - s/16.00");
        System.out.println("Ingrese opción: ");
        eleccion = sc.nextInt();
        if (eleccion >= 1 && eleccion <= 10) {
            System.out.println("Cantidad: ");
            cantidad = sc.nextInt();
            switch (eleccion) {
                case 1: CafeCalientetotal = 15.50 * cantidad; break;
                case 2: CafeCalientetotal = 15.00 * cantidad; break;
                case 3: CafeCalientetotal = 13.00 * cantidad; break;
                case 4: CafeCalientetotal = 15.00 * cantidad; break;
                case 5: CafeCalientetotal = 15.50 * cantidad; break;
                case 6: CafeCalientetotal = 14.50 * cantidad; break;
                case 7: CafeCalientetotal = 15.50 * cantidad; break;
                case 8: CafeCalientetotal = 15.50 * cantidad; break;
                case 9: CafeCalientetotal = 14.50 * cantidad; break;
                case 10: CafeCalientetotal = 16.00 * cantidad; break;
            }
            System.out.println("Precio Café Caliente: S/ " + CafeCalientetotal);
            String nombre = getNombreCafeCaliente(eleccion);
            detalleCompra.add(nombre + " x" + cantidad + " = S/" + CafeCalientetotal);
        } else {
            System.out.println("Opción fuera de rango");
        }
        return CafeCalientetotal;
    }
    public static double menuAlimentos(Scanner l, ArrayList<String> detalleCompra) {
        double eleccion = 0;
        int opcion;

        do {
            System.out.println("--- A L I M E N T O S----");
            System.out.println("1. Pastries");
            System.out.println("2. Sandwiches");
            System.out.println("3. Terminar selección de alimentos");
            System.out.println("Seleccione opcion: ");
            opcion = l.nextInt();
            switch (opcion) {
                case 1:
                    eleccion += menuPastries(l, detalleCompra);
                    break;
                case 2:
                    eleccion += menuSandwiches(l, detalleCompra);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opcion != 3);
        return eleccion;
    }

    public static double menuPastries(Scanner l, ArrayList<String> detalleCompra) {
        int opcion, cantidad;
        double precio = 0;
        System.out.println("--------P A S T R I E S-------");
        System.out.println("1. Galleta Rellena de Crema de Avellanas - S/11.00");
        System.out.println("2. Muffin de Naranja & Chocochips - S/10.50");
        System.out.println("3. Keke de Zanahoria - S/10.50");
        System.out.println("4. Galleta de Avena & Cranberry - S/7.50");
        System.out.println("5. Keke de Limon - S/10.50");
        System.out.println("6. Galleta de Chocochips - S/7.50");
        System.out.println("7. Muffin de Berries - S/10.50");
        System.out.println("8. Egg Bites (2 un) - S/10.00");
        System.out.println("9. Galleta Mom - S/7.00");
        System.out.println("10. Cake Pop Mom - S/7.00");
        System.out.println("Seleccione producto: ");
        opcion = l.nextInt();
        if (opcion < 1 || opcion > 10) {
            System.out.println("Opcion invalida");
            return 0;
        }
        System.out.println("Ingrese cantidad: ");
        cantidad = l.nextInt();
        switch (opcion) {
            case 1: precio = 11.00; break;
            case 2: precio = 10.50; break;
            case 3: precio = 10.50; break;
            case 4: precio = 7.50; break;
            case 5: precio = 10.50; break;
            case 6: precio = 7.50; break;
            case 7: precio = 10.50; break;
            case 8: precio = 10.00; break;
            case 9: precio = 7.00; break;
            case 10: precio = 7.00; break;
        }
        double subtotal = precio * cantidad;
        System.out.println("Compra Añadida");
        System.out.println("Precio total en pastries: " + subtotal);
        String nombre = getNombrePastries(opcion);
        detalleCompra.add(nombre + " x" + cantidad + " = S/" + subtotal);
        return subtotal;
    }
    public static double menuSandwiches(Scanner l, ArrayList<String> detalleCompra) {
        int opcion, cantidad;
        double precio = 0;
        System.out.println("========= S A N D W I C H E S =========");
        System.out.println("1. Sandwich Finas Hierbas - S/17.50");
        System.out.println("2. Croissant de Mantequilla - S/7.50");
        System.out.println("3. Sandwich Croissant Jamon del Pais & Queso - S/16.50");
        System.out.println("4. Sandwich Croissant Jamon Ingles & Queso - S/16.50");
        System.out.println("5. Sandwich Pavita & Queso - S/14.50");
        System.out.println("6. Sandwich Eggmont - S/13.50");
        System.out.println("7. Sandwich Pavita, Queso & Espinaca - S/15.00");
        System.out.println("8. Sandwich Brioche Campesino - S/16.00");
        System.out.println("9. Sandwich Panino Vesubio - S/13.50");
        System.out.println("10. Sandwich Chicken Panino - S/14.50");
        System.out.println("Seleccione producto: ");
        opcion = l.nextInt();
        if (opcion < 1 || opcion > 10) {
            System.out.println("Opcion invalida");
            return 0;
        }
        System.out.println("Ingrese cantidad: ");
        cantidad = l.nextInt();
        switch (opcion) {
            case 1: precio = 17.50; break;
            case 2: precio = 7.50; break;
            case 3: precio = 16.50; break;
            case 4: precio = 16.50; break;
            case 5: precio = 14.50; break;
            case 6: precio = 13.50; break;
            case 7: precio = 15.00; break;
            case 8: precio = 16.00; break;
            case 9: precio = 13.50; break;
            case 10: precio = 14.50; break;
        }
        double subtotal = precio * cantidad;
        System.out.println("Compra Añadida");
        System.out.println("Precio total en sandwiches: " + subtotal);
        String nombre = getNombreSandwich(opcion);
        detalleCompra.add(nombre + " x" + cantidad + " = S/" + subtotal);

        return subtotal;
    }

    // ==================== metodos auxiliares ====================
    private static String getNombreFrappuccino(int op) {
        switch (op) {
            case 1: return "Black & White Mocha Frappuccino";
            case 2: return "Cookies & Cream Creme Frappuccino";
            case 3: return "Ultimate Caramel Frappuccino";
            case 4: return "Mocha Frappuccino";
            case 5: return "Chocolúcuma Frappuccino";
            case 6: return "Caramel Frappuccino";
            case 7: return "Triple Mocha Frappuccino";
            case 8: return "Algarrobina Frappuccino";
            case 9: return "Chocolate Creme Frappuccino";
            case 10: return "Lúcuma Creme Frappuccino";
            default: return "Frappuccino";
        }
    }
    private static String getNombreCafeCaliente(int op) {
        switch (op) {
            case 1: return "Flat White";
            case 2: return "Latte";
            case 3: return "Latte Macchiato";
            case 4: return "Caramel Macchiato";
            case 5: return "Mocha Café";
            case 6: return "Vainilla Latte";
            case 7: return "Skinny Vainilla Latte";
            case 8: return "Mocha Blanco Café";
            case 9: return "Algarrobina Latte";
            case 10: return "Americano";
            default: return "Café Caliente";
        }
    }
    private static String getNombrePastries(int op) {
        switch (op) {
            case 1: return "Galleta Rellena de Crema de Avellanas";
            case 2: return "Muffin de Naranja & Chocochips";
            case 3: return "Keke de Zanahoria";
            case 4: return "Galleta de Avena & Cranberry";
            case 5: return "Keke de Limon";
            case 6: return "Galleta de Chocochips";
            case 7: return "Muffin de Berries";
            case 8: return "Egg Bites (2 un)";
            case 9: return "Galleta Mom";
            case 10: return "Cake Pop Mom";
            default: return "Pastries";
        }
    }
    private static String getNombreSandwich(int op) {
        switch (op) {
            case 1: return "Sandwich Finas Hierbas";
            case 2: return "Croissant de Mantequilla";
            case 3: return "Sandwich Croissant Jamon del Pais & Queso";
            case 4: return "Sandwich Croissant Jamon Ingles & Queso";
            case 5: return "Sandwich Pavita & Queso";
            case 6: return "Sandwich Eggmont";
            case 7: return "Sandwich Pavita, Queso & Espinaca";
            case 8: return "Sandwich Brioche Campesino";
            case 9: return "Sandwich Panino Vesubio";
            case 10: return "Sandwich Chicken Panino";
            default: return "Sandwich";
        }
    }
    // ====================  ====================
    public static String elegirDepartamento(Scanner sc) {
        String departamento = "";
        System.out.println("======= DEPARTAMENTO DE REGISTRO =======");
        System.out.println("1. Lima");
        System.out.println("2. Arequipa");
        System.out.println("3. Cusco");
        System.out.println("4. Trujillo");
        System.out.println("5. Piura");
        System.out.println("6. Puno");
        System.out.println("7. Tacna");
        System.out.println("Seleccione su departamento:");
        int opcion = sc.nextInt();
        sc.nextLine();
        switch (opcion) {
            case 1: departamento = "Lima"; break;
            case 2: departamento = "Arequipa"; break;
            case 3: departamento = "Cusco"; break;
            case 4: departamento = "Trujillo"; break;
            case 5: departamento = "Piura"; break;
            case 6: departamento = "Puno"; break;
            case 7: departamento = "Tacna"; break;
            default:
                System.out.println("Opcion no valida.");
                System.exit(0);
        }
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

        String tienda = "";
        System.out.println("Seleccione el departamento donde recoger:");
        System.out.println("1. Lima");
        System.out.println("2. Arequipa");
        System.out.println("3. Cusco");
        System.out.println("4. Trujillo");
        System.out.println("5. Piura");
        System.out.println("Seleccione:");
        int opcion = sc.nextInt();
        sc.nextLine();
        switch (opcion) {
            case 1: tienda = "Starbucks Lima"; break;
            case 2: tienda = "Starbucks Arequipa"; break;
            case 3: tienda = "Starbucks Cusco"; break;
            case 4: tienda = "Starbucks Trujillo"; break;
            case 5: tienda = "Starbucks Piura"; break;
            default:
                System.out.println("Opcion no valida.");
                System.exit(0);
        }
        System.out.println("Recoges en: " + tienda);
        return tienda;
    }

    public static String validarPagoTarjeta(Scanner sc, double total) {

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
                int mes = Integer.parseInt(partes[0]);
                int anio = Integer.parseInt(partes[1]);
                int anioActual = 26;
                int mesActual = 5;
                if (mes >= 1 && mes <= 12) {
                    if (anio > anioActual || (anio == anioActual && mes >= mesActual)) {
                        fechaOK = true;
                    }
                }
            }
            if (tarjetaOK && fechaOK && cvvOK) {
                System.out.println("A U T O R I Z A N D O    F O N D O S...");
                System.out.println("Transaccion exitosa. Pago aprobado!");
                pagoAprobado = true;
            } else {
                System.out.println("Operacion denegada. Verifique sus datos:");
                if (!tarjetaOK) System.out.println("La tarjeta debe tener 16 digitos.");
                if (!fechaOK) System.out.println("Targeta Caducada.");
                if (!cvvOK) System.out.println("El CVV debe tener 3 digitos.");
                System.out.println("Intente nuevamente.");
            }
        } while (!pagoAprobado);
        String numeroOculto = "************" + numeroTarjeta.substring(12);
        return numeroOculto;
    }

    public static void generarBoleta(String nombre, String dni, String correo,
                                     String telefono, String departamento,
                                     double total, String tarjeta, String tienda) {

        double subtotal = total / 1.18;
        double igv = total - subtotal;
        String numeroBoleta = "B001-00" + (int) (Math.random() * 900 + 100);
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("============================================");
        System.out.println("             STARBUCKS COFFEE                 ");
        System.out.println("             RUC: 20100070970                   ");
        System.out.println("   Av. Benavides 415, Miraflores - Lima     ");
        System.out.println("             Tel: (01) 242-2600                 ");
        System.out.println("============================================");
        System.out.println("              BOLETA DE VENTA                 ");
        System.out.println("  Boleta N: " + numeroBoleta);
        System.out.println("  Fecha: " + fecha + "   Hora: " + hora);
        System.out.println("--------------------------------------------");
        System.out.println("  DATOS DEL CLIENTE:");
        System.out.println("  Nombre      : " + nombre);
        System.out.println("  DNI         : " + dni);
        System.out.println("  Correo      : " + correo);
        System.out.println("  Telefono    : " + telefono);
        System.out.println("  Departamento: " + departamento);
        System.out.println("--------------------------------------------");
        System.out.println("  Subtotal sin IGV : S/ " + (Math.round(subtotal * 100) / 100.0));
        System.out.println("  IGV (18%)         : S/ " + (Math.round(igv * 100) / 100.0));
        System.out.println("  TOTAL             : S/ " + total);
        System.out.println("--------------------------------------------");
        System.out.println("  Metodo de pago  : Tarjeta " + tarjeta);
        System.out.println("  Tienda de recojo: " + tienda);
        System.out.println("============================================");
        System.out.println("     Gracias por tu visita a Starbucks!      ");
        System.out.println("    Esperamos verte pronto. Have a nice day!");
        System.out.println("============================================");
    }
}