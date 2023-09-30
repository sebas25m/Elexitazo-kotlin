package com.example.elexitazo_kotlin
/*
@author Sebastian Montoya Rosario
   Fecha de creación: 20/09/2023
   Funcionalidad: Este programa pide los datos del usuario para un almacén, luego le pregunta cuantos
   productos desea comprar y le da el total de la compra.
*/
//Cargamos la biblioteca Gson
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException

//Variables y constantes
//Con estas listas se almacenan los datos del usuario al momento de crear una cuenta
val dato = arrayListOf(
    "nombre: ",
    "edad: ",
    "telefono: ",
    "dirección de vivienda: ",
    "correo: ",
    "contraseña: "
)

var datos_usuario = mutableListOf<String>()

//Estas listas mutables almacenan los precios de los producto que
var productos19 = mutableListOf<Int>()
var producto5 = mutableListOf<Int>()
var prouctosfamilar = mutableListOf<Int>()

// Almacena el json deserializado
val categorias = cargarProductos()

//Esta lista almacena un diccionario o un 'map' que contiene el nombre, precio unitario y precio total de los producto que ha seleccionado el cliente
var productos_comprados = mutableListOf<MutableMap<String, Any?>>()

//Variables globales que almacenan el numero de categoria y subcategoria que el cliente desea ver
var globaleleccionCategoria = 0
var globalSubcategoria = 0
var total = 0

//Almacenamiento de datos bancarios del usuario
var datos_compra = mutableListOf<String>()

inline fun <reified T> cargarJSON(archivo: File): T {
    /*Funcion para cargar algún json*/
    //Para leer el contenido del archivo json
    val jsonStr = archivo.readText()

    //Convertur el JSON en un objeto de kotlin
    val gson = Gson()
    return gson.fromJson(jsonStr, T::class.java)
}

/*Usamos la funcion para cargar el json y lo deserializamos en la variable
canasta_familiar donde se guardan todos los productos referentes a el nombre de la variable*/
val canasta_familiar =
    cargarJSON<FamiliarWrapper>(archivo = File("C:/Users/monto/AndroidStudioProjects/Elexitazokotlin/app/src/main/assets/canasta_familiar.json"))

/*Ahora deserializamos el archivo que contiene los productos con iva del 5%*/
val productos_iva5 =
    cargarJSON<Percent5Wrapper>(archivo = File("C:/Users/monto/AndroidStudioProjects/Elexitazokotlin/app/src/main/assets/productos5%.json"))

/*Ahora deserializamos el archivo que contiene los productos con iva del 19%*/
val productos_iva19 =
    cargarJSON<productosIVA19Wrapper>(archivo = File("C:/Users/monto/AndroidStudioProjects/Elexitazokotlin/app/src/main/assets/productos19%.json"))

fun cargarProductos(): List<Categoria> {
    // Cargamos los JSON usando la función
    val archivo =
        File("C:/Users/monto/AndroidStudioProjects/Elexitazokotlin/app/src/main/assets/Productos.json")

    try {
        val categoriasWrapper = cargarJSON<CategoriasWrapper>(archivo)
        return categoriasWrapper.categorias
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        println("Asegúrate de haber cargado correctamente los archivos del paquete")
        return emptyList() // Devolver una lista vacía en caso de excepción
    }
}


fun check_num(mensaje: String): Long {
    /*Esta función controla el  error que puede ocurrir al momento de que el usuario pueda escribir válores no numericos*/
    while (true) {
        try {
            //Se pregunta algo al usuario dentro de el parametro 'mensaje'
            print(mensaje)
            //Se lee como un dato de tipo string
            val valor = readln()
            //Se intenta convertir a entero ese string
            val valornum = valor.toLong()
            return valornum
        } catch (e: NumberFormatException) {
            //Si el contenido de ese String es un válor no númerico, entonces mostrara el siguiente mensaje
            println("Debes ingresar un válor númerico.")
            //Se volvera a retpetir el bucle mientras el valor que escribe el usuario no sea númerico
            continue
        }
    }
}

fun cadena_llena(mensaje: String): String {
    /*En esta función se controla  el caso en donde el usuario no escriba ningun dato al momento de preguntarle y solo le de enter*/
    while (true) {
        //Se le pide al usuario algún dato
        print(mensaje)
        //Se lee el dato proporcionado por el
        val usuario_pmt = readln()
        if (usuario_pmt.isNotEmpty()) {
            //Si la variable que almacena el válor que proporciono el usuairo no esta vacia, se retorna
            return usuario_pmt
        } else {
            //De lo contrario se mostrara el mensaje y se volvera a ejecutar la función
            print("No has ingresado ningún dato.")
        }
    }
}

fun check_mail(mensaje: String): String {
    //Se crea un patron de caracteres que debe tener una dirección de correo
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
    val pattern = Regex(emailRegex)

    while (true) {
        //Se llama la función cadena llena para que la informacion que proporciona el usuario no este vacia
        val correo = cadena_llena(mensaje)
        //Con la funcion matches podemos comparar una serie de patrones que puede contener un String
        if (pattern.matches(correo)) {
            //Si la informacion y el patron coinciden el programa retorna el valor del correo
            println("E-mail válido")
            return correo
        } else {
            //Si la informacion y el patron no coinciden se volvera a ejecutar el bucle
            println("Debes escribir una dirección de correo válida.")
            continue
        }
    }
}

fun check_edad(): Int {
    /*En esta función se evalua la edad de una persona, el programa no admite menores de edad*/
    while (true) {
        //Se lee la edad y se asegura de que el dato escrito sea númerico
        val edad = check_num("Escribe tu edad: ")
        //La condicion consiste en si la edad es menor igual a 17 o mayor igual a 100 puesto que la edad que escribio el usario debe estar dentro de ese rango para que lo deje ingresar
        if (edad >= 100 || edad <= 17) {
            if (edad <= 17) {
                println("Debes ser mayor de edad para acceder.")
            } else {
                println("Edad fuera de rango real.")
            }
        } else {
            //Si la edad es permitida retorna ese valor
            return edad.toInt()
        }
    }
}

fun recopilar_Datos() {
    /*Esta función permite al usuario registrarse en el programa y guardar sus datos*/
    for (i in dato) {
        //Automatizamos la recopilacion de datos con un for
        when (i) {
            "edad: " -> {
                datos_usuario.add(check_edad().toString())
            }

            "telefono: " -> {
                datos_usuario.add(check_num("Escribe tu número de teléfono: ").toString())
            }

            "correo: " -> {
                datos_usuario.add(check_mail("Escribe tu dirección de correo electrónico: "))
            }

            else -> {
                print("Ingresa tu ${i}: ")
                datos_usuario.add(readln())
            }
        }
    }
    inicio_sesion()
}

fun inicio_sesion() {
    //Usamos la función size para poder saber la longitud de la lista datos_usuario
    if (datos_usuario.size == 0) {
        //Se evalua si la longitud de la lista es 0 por si no se ha creado ninguna cuenta  aun
        println("No has creado alguna cuenta aún, redireccionando a al regístro de datos.")
        //Se redirecciona al usuario a la funcion donde se leen los datos para que cree una cuenta
        recopilar_Datos()
    } else {
        //Si ya hay una cuenta creada se le pide al usuario confirmar su correo y contraseña
        while (true) {
            print("Escribe tu dirección de correo electrónico: ")
            val correo = readln()
            print("Escribe tu contraseña: ")
            val password = readln()
            //Se leen los datos y se comparan con lo que el usuario escribio para saber si puede ingresar al programa o no
            if (password.equals(datos_usuario[datos_usuario.size - 1]) && correo.equals(
                    datos_usuario[datos_usuario.size - 2], ignoreCase = true
                )
            ) {
                println("Has iniciado sesión exitosamente.")
                break
            } else {
                println("Correo o contraseña incorrectos. \nVuelve a intentarlo.")
                continue
            }
        }
    }
}

fun mostrar_categorias(): Int {
    val categorias = cargarProductos()

    categorias.forEachIndexed { indice, elemento ->
        println("${indice + 1}. ${elemento.nombre}")
    }
    println("9. Volver al menú principal\n10. Ir al carrito de compras")
    var eleccionCategoria = 0
    while (true) {
        eleccionCategoria = check_num("Elige una categoría (1 - 9): ").toInt()
        if (eleccionCategoria in (1..8)) {
            break
        } else if (eleccionCategoria == 9) {
            println("Volviendo al menú principal...")
            break
        } else if (eleccionCategoria == 10) {
            println("Mostrando el carrito de compras...")
            break
        } else {
            println("Debes elegir una opción válida.")
        }
    }
    return eleccionCategoria // Devuelve la elección de categoría del usuario
}


fun mostrar_subcategorias(categorias: List<Categoria>, eleccionCategoria: Int): Int {
    if (eleccionCategoria < 1 || eleccionCategoria > categorias.size) {
        println("Selección de categoría no válida.")
        return 0
    }

    val categoria = categorias[eleccionCategoria - 1]
    println("Subcategorías de ${categoria.nombre}:")

    for ((index, subcategoria) in categoria.subcategorias.withIndex()) {
        println("${index + 1}. ${subcategoria.nombre}")
    }

    println("${categoria.subcategorias.size + 1}. Volver a categorías")
    println("${categoria.subcategorias.size + 2}. Ir al carrito de compras")

    var eleccionSubcategoria = 0
    while (true) {
        eleccionSubcategoria =
            check_num("Elige una subcategoría (1 - ${categoria.subcategorias.size + 2}): ").toInt()
        if (eleccionSubcategoria in 1..(categoria.subcategorias.size + 2)) {
            if (eleccionSubcategoria == categoria.subcategorias.size + 1) {
                // Volver a categorías
                return 0
            } else if (eleccionSubcategoria == categoria.subcategorias.size + 2) {
                println("Mostrando el carrito de compras...")
                return 0
            } else {
                return eleccionSubcategoria
            }
        } else {
            println("Debes elegir una opción válida.")
        }
    }
}


fun comprar_producto(producto: Producto) {
    /*Esta fucnion le permite al usuario comprar un producto y cargarlo a la lista donde se almacenan sus productos*/
    println("Detalles del producto seleccionado: ")
    //Atributos de los productos, se utiliza para que elp cliente sepa que producto va a comprar
    println("Nombre: ${producto.nombre}")
    println("Descripción: ${producto.descripcion}")
    println("Precio: $${producto.precio}")
    println("1. Agregar al carrito y comprar mas productos. \n2. Agregar al carrito y ver el carrito de compras\n3. Volver atrás")
    while (true) {
        //Bucle que se cumple en caso de que el usuario eligauna opción inváida
        val opcion = check_num("Elige una opción: ").toInt()
        if (opcion == 1 || opcion == 2) {
            val cantidad =
                check_num("Escribe cuantos items deseas de el producto ${producto.nombre}: ")
            val total = cantidad * producto.precio

            // Se crea un diccionario con información del producto seleccionado
            val itemComprado = mutableMapOf<String, Any?>(
                "nombre" to producto.nombre,
                "cant. items" to cantidad,
                "Precio" to producto.precio,
                "Precio total" to total
            )

            // Agregar el diccionario a la lista de productos comprados
            productos_comprados.add(itemComprado)
            if (opcion == 1) {
                mostrar_productos(categorias[globaleleccionCategoria - 1].subcategorias[globalSubcategoria - 1])
                break
            } else {
                //Se muestra el carrito de compras
                print("Ingresando al carrito de compras...")
                carrito_compras()
                break
            }
        } else if (opcion == 3) {
            //El usuario vuelve a visualizar los productos disponibles dentro de la categoria que selecciono
            mostrar_productos(categorias[globaleleccionCategoria - 1].subcategorias[globalSubcategoria - 1])
            break
        } else {
            print("Debes elegir una opción válida.")
        }
    }
}

fun mostrar_productos(subcategoriaSeleccionada: Subcategorias) {
    println("Productos de ${subcategoriaSeleccionada.nombre}:")

    val productos = subcategoriaSeleccionada.Productos

    for ((indice, producto) in productos.withIndex()) {
        println("${indice + 1}. Nombre: ${producto.nombre}, Descripción: ${producto.descripcion}, Precio: $${producto.precio}")
    }

    println("${productos.size + 1}. Volver atrás")

    val eleccionProducto = check_num("Elige un producto (1 - ${productos.size + 1}): ")

    if (eleccionProducto in 1..productos.size) {
        comprar_producto(productos[eleccionProducto.toInt() - 1])
    } else if (eleccionProducto.toInt() == productos.size + 1) {
        // Volver atrás
        mostrar_subcategorias(categorias, globaleleccionCategoria)
    } else {
        println("Debes elegir una opción válida.")
    }
}

fun carrito_compras() {
    //Con esta función el usuario puede ver los productos que ha seleccionado hasta el momento
    if (productos_comprados.size == 0) {
        print("El carrito de compras esta vacío!!\nRedireccionando al seccion de cátegorias...")
        globaleleccionCategoria = mostrar_categorias()
        if (globaleleccionCategoria != -1) {
            globalSubcategoria =
                mostrar_subcategorias(categorias, globaleleccionCategoria)
            if (globalSubcategoria != -1) {
                mostrar_productos(categorias[globaleleccionCategoria - 1].subcategorias[globalSubcategoria - 1])
            }
        }
    } else {
        println("Productos seleccionados: \nNombre del producto    -  Cantidad items  -   Precio Unitario   -   Precio Total")

        //Se muestran los productos que el cliente ha seleccionado
        productos_comprados.forEachIndexed { indice, producto ->
            println("${indice + 1}. ${producto["nombre "]}  ${producto["cant. items "]} ${producto["Precio "]} ${producto["Precio total "]}")
        }

        total = calcular_pago()
        println("Total de la compra (Sujeto a cambios de IVA): $total")
        pasarela_pago()
    }
}

fun calcular_pago(): Int {

    //En esta funcion el cliente puede saber cuanto costo su compra
    for (itemComprado in productos_comprados) {
        //itemComprado lee toda la lista de los nombres de productos seleccionados por el cliente
        val nombreProducto = itemComprado["nombre"]
        //Se evalua si algún producto se encuentra dentro de la lista de productos con iva del 19, 5 o si son de la canasta familiar
        var encontrado = productos_iva19.productosIVA19.any { it.producto == nombreProducto }
        if (encontrado) {
            /*Si el nombre se encuentra en alguna lista el precio total de la compra se le agrega a una lista que
            contiene todos los productos con el iva correspondiente, en este caso, si se encuentra dentro de los productos
             con el 19% se agregara a la  lista 'productos19'*/
            productos19.add(itemComprado["Precio total"] as Int)
            println("El producto $nombreProducto está sujeto al IVA del 19%.")
        } else {
            encontrado = productos_iva5.productosIVA5.any { it.productos == nombreProducto }
            if (encontrado) {
                producto5.add(itemComprado["Precio total"] as Int)
                println("El producto $nombreProducto está sujeto al IVA del 5%.")
            } else {
                encontrado =
                    canasta_familiar.productos_canf.any { it.productos == nombreProducto }
                if (encontrado) {
                    prouctosfamilar.add(itemComprado["Precio total"] as Int)
                    println("El producto $nombreProducto no está sujeto al IVA porque pertenece a la canasta familar.")
                } else {
                    println("El producto $nombreProducto no se encontro..")
                }
            }
        }
    }
    val subtotal = productos19.sum() + producto5.sum() + prouctosfamilar.sum()
    println("El subtotal de tu compra es (Precio no sujeto a IVA): $$subtotal")
    /*Con esta función lambda calculamos el total, sumamos cada lista
    y sacamos el porcentaje de iva para cada una, luego la sumamos con el precio
    base que es la suma de cada lista de productos por  separado, ya teniendo el precio con los porcentajes sumamos todos los
    resultados para poder sbaer el total de toda la compra*/
    return ((productos19.sum() * 0.19) + productos19.sum()).toInt() + ((producto5.sum() * 0.05) + producto5.sum()).toInt() + prouctosfamilar.sum()
}

fun pasarela_pago() {
    if (datos_usuario.size == 0) {
        println("No te has regístrado aún, redireccionando al log-in...")
        recopilar_Datos()
    }
    while (true) {
        println("1. Pago contra-entrega\n2. Pago con tarjeta débito\n3. Pago con tarjeta crédito\n4. Pago con PSE")
        val opcion = check_num("Elige la opción con la que desees pagar: ").toInt()
        if (opcion == 1) {
            println("Has seleccionado con pago contra-entrega...")
        } else if (opcion == 2 || opcion == 3) {
            var pago = ""
            if (opcion == 2) {
                pago = "tarjeta débito"
            } else {
                pago = "tarjeta crédito"
            }
            println("Has seleccionado pago con ${pago}...")
            datos_compra.add(check_num("Escribe tu número de tarjeta: ").toString())
            datos_compra.add(check_num("Escribe el código de seguridad de la tarjeta: ").toString())
            datos_compra.add(cadena_llena("Escribe el nombre del titular: "))
        } else if (opcion == 4) {
            print("Has seleccionado pago por PSE...")
            datos_compra.add(cadena_llena("Escribe el nombre del banco: "))
            datos_compra.add(check_num("Escribe tu número de cuenta: ").toString())
            print("1. Cuenta de ahorro\n2. Cuenta corriente")
            datos_compra.add(check_num("Elige una opción de tipo de cuenta: ").toString())
            print("1. Cédula de ciudadanía\n2. NIT")
            datos_compra.add(check_num("Elige una opción: ").toString())
            datos_compra.add(check_num("Escribe tu numero de documento: ").toString())
        }
        var indice = 0
        println("DATOS PERSONALES")
        for (datos in dato) {
            println("$datos ${datos_usuario[indice]}")
            indice += 1
        }
        println("DATOS BANCARIOS")
        for (banco in datos_compra) {
            println(banco)
        }
        println("1. Confirmar datos y generar factura\n2. Volver a escribir mis datos\n3. Cancelar compra")
        val opc = check_num("Elige un opción: ").toInt()

        if (opc == 1) {
            println("compra realizada.\n volviendo al menu principal...")
            main()
            break
        } else if (opc == 2) {
            continue //Volver a reiniciar la funcion
        } else if (opc == 3) {
            println("Cancelando compra...")
            main()
            break
        }
    }
}

fun main() {
    var salir = false
    println("Bienvenido por al exitazo. \n Este programa simula el sistema de un supermercado en donde puedes crear una cuenta, registrate, seleccionar producto, etc.")
    while (!salir) {
        println("Menú Principal:")
        println("1. Ir a la sección de compras")
        println("2. Iniciar sesión")
        println("3. Registrarme")
        println("4. Salir del programa")

        val opcionMenuPrincipal = check_num("Elige una opción (1 - 4): ").toInt()

        when (opcionMenuPrincipal) {
            1 -> {
                val categorias = cargarProductos()
                println("Aqui puedes comprar los productos que desees productos.")
                while (true) {
                    globaleleccionCategoria = mostrar_categorias()
                    if (globaleleccionCategoria != -1) {
                        globalSubcategoria =
                            mostrar_subcategorias(categorias, globaleleccionCategoria)
                        if (globalSubcategoria != -1) {
                            mostrar_productos(categorias[globaleleccionCategoria - 1].subcategorias[globalSubcategoria - 1])
                        } else {
                            break // Volver atrás desde las subcategorías
                        }
                    } else {
                        break // Volver atrás desde las categorías
                    }
                }
            }

            2 -> {
                inicio_sesion()
                println("Aqui puedes comprar los productos que desees productos.")
                while (true) {
                    globaleleccionCategoria = mostrar_categorias()
                    if (globaleleccionCategoria != -1) {
                        globalSubcategoria =
                            mostrar_subcategorias(categorias, globaleleccionCategoria)
                        if (globalSubcategoria != -1) {
                            mostrar_productos(categorias[globaleleccionCategoria - 1].subcategorias[globalSubcategoria - 1])
                        } else {
                            break // Volver atrás desde las subcategorías
                        }
                    } else {
                        break // Volver atrás desde las categorías
                    }
                }
            }

            3 -> {
                recopilar_Datos()
                println("Aqui puedes comprar los productos que desees productos.")
                while (true) {
                    globaleleccionCategoria = mostrar_categorias()
                    if (globaleleccionCategoria != -1) {
                        globalSubcategoria =
                            mostrar_subcategorias(categorias, globaleleccionCategoria)
                        if (globalSubcategoria != -1) {
                            mostrar_productos(categorias[globaleleccionCategoria - 1].subcategorias[globalSubcategoria - 1])
                        } else {
                            break // Volver atrás desde las subcategorías
                        }
                    } else {
                        break // Volver atrás desde las categorías
                    }
                }
            }

            4 -> {
                println("Saliendo del programa...\nCreado por: Sebastian Montoya Rosario")
                salir = true
            }

            else -> println("Debes elegir una opción válida.")
        }
    }
}
