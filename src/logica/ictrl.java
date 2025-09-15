/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Juanpi
 */

import java.util.Date;

import persistencia.ControladoraPersistencia;

import java.util.List;

//para cargar datos automaticos
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;

//semillero
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;

public class ictrl{
    
    private ControladoraPersistencia controlP = new ControladoraPersistencia(); //no se si instanciar eso aca o como tenerlo.
    
    protected ictrl(){
        
    }
    
    //ControladoraPersistencia controlP = new ControladoraPersistencia(); //donde tengo los metodos para controlar la persistencia
    
    public void altaPerfilProponente(String nickname, String nombre, String apellido, String correo, byte[] imagenBytes, String direccion, String biografia, String linkSitio){
    //imagen opcional
    //si el nickname o el correo esta en uso o un campo vacio o formato incorrecto, el sistema avisa.
    
    //una vez creado le paso el usuario que sea a controlP
    proponente p = new proponente(nickname, nombre, apellido, correo, imagenBytes, direccion, biografia, linkSitio);
    //controlP.crearUsuario(u);
    controlP.crearUsuarioProponente(p);
    }
    
    public List<proponente> listarProponentes(){
    return controlP.listarProponentes();
    }
    
    public void altaPerfilColaborador(String nickname, String nombre, String apellido, String correo, byte[] imagenBytes){
    colaborador c = new colaborador(nickname, nombre, apellido, correo, imagenBytes);
    controlP.crearUsuarioColaborador(c);
    }
    
    public List<colaborador> listarColaboradores(){
    return controlP.listarColaboradores();
    }
    
    public void altaCategoria(String nombre){
    categoria cat = new categoria(nombre);
    categoria pad = controlP.GetCategoria("Categoria");
    cat.setPadre(pad);
    controlP.crearCategoria(cat);
    }
    
    public void altaSubCategoria(String nombre, String padre){
    categoria cat = new categoria(nombre);
    categoria pad = controlP.GetCategoria(padre);
    if(pad == null){
    throw new IllegalArgumentException("No existe el padre:" + padre);
    }
    cat.setPadre(pad);
    controlP.crearCategoria(cat);
    
    }
    
    public List<categoria> listarCategoria(){
    return controlP.listarCategoria();
    }
    
    public void altaPropuesta(proponente proponente, String titulo, String descripcion, String tipoEspectaculo, String lugar, Date fechaRealizacion, float precioEntrada, float montoNecesario, String tipoRetorno, byte[] imagenBytes, estadoPropuesta estado){
    propuesta prop = new propuesta(proponente, titulo, descripcion, tipoEspectaculo, lugar, fechaRealizacion, precioEntrada, montoNecesario, tipoRetorno);
    prop.setImagen(imagenBytes);
    prop.setEstado(estado);
    controlP.crearPropuesta(prop);
    }
    
    public List<propuesta> listarPropuestas() {
        return controlP.listarPropuestas();
    }
    
    public void modificoPropuesta(propuesta prop){
    controlP.modificoPropuesta(prop);
    }
    
    public void altaColaboracion(colaborador colab, propuesta prop, Float monto, String retorno){
        
        //tengo q actualizar el montorecaudado en la prop (a ver si asi funciona testear)
        prop.setMontoRecaudado(monto);
        //cambiar estado  de propuesta dependiendo
        if(prop.getEstadoActual().toString().equalsIgnoreCase("INGRESADA")){
        prop.setEstado(estadoPropuesta.EN_FINANCIACION);
        }else if(prop.getEstadoActual().toString().equalsIgnoreCase("EN_FINANCIACION")){
            Float total = prop.getRecaudado() + monto;
        if(total >= prop.getMonto()){
        prop.setEstado(estadoPropuesta.FINANCIADA);
        }
        }
        
        controlP.modificoPropuesta(prop);
        
    colaboracion col = new colaboracion(colab, prop, monto, retorno);
    controlP.crearColaboracion(col);
    }
    
    public void altaColaboracion2(colaborador colab, propuesta prop, Float monto, String retorno, Date fecha){ //usado para cargar los datos precargados esos
    
        prop.setMontoRecaudado(monto);
        //cambiar estado  de propuesta dependiendo
        if(prop.getEstadoActual().toString().equalsIgnoreCase("INGRESADA")){
        prop.setEstado(estadoPropuesta.EN_FINANCIACION);
        }else if(prop.getEstadoActual().toString().equalsIgnoreCase("EN_FINANCIACION")){
            Float total = prop.getRecaudado() + monto;
        if(total >= prop.getMonto()){
        prop.setEstado(estadoPropuesta.FINANCIADA);
        }
        }
        
        prop.setFecha(fecha); //esto agregue. faltaria LA HORA.
        
        controlP.modificoPropuesta(prop);
        
    colaboracion col = new colaboracion(colab, prop, monto, retorno);
    controlP.crearColaboracion(col);
    
    }
    
    public List<colaboracion> listarColaboraciones(){
    return controlP.listarColaboraciones();
    }
    
    public void eliminoColab(int id){
    controlP.eliminoColaboracion(id);
    }
    
    public List<usuario> listarUsuarios(){
    return controlP.listarUsuarios();
    }
    
    public void seguirUsuario(String seguidorNick, String seguidoNick){
    
        //
        controlP.seguirUsuario(seguidorNick, seguidoNick);
        
    }
    
    public void dejarSeguir(String seguidorNick, String seguidoNick){
    
         //
        controlP.dejarDeSeguir(seguidorNick, seguidoNick);
        
    }
    
    //cargar datos automaticos con un boton. EN PRUEBA.

public static final class Fotos {
    private static final String[] EXTENSIONES = {"png", "jpg", "jpeg"};

    private Fotos() {}

    /** Carga la imagen {baseName}.{png|jpg|jpeg} desde 'carpeta', la escala al "box" y la devuelve como PNG en bytes. */
    public static byte[] cargarComoPNG(Path carpeta, String baseName, int boxW, int boxH, boolean permitirUpscale) throws IOException {
        Path ruta = resolverArchivo(carpeta, baseName);
        if (ruta == null)
            return null;  

        BufferedImage src = ImageIO.read(ruta.toFile());
        if (src == null)
            return null;

        BufferedImage scaled = escalar(src, boxW, boxH, permitirUpscale);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(scaled, "png", baos);
            return baos.toByteArray();
        }
    }

    /** Igual que arriba pero SIN escalado: devuelve PNG con tamaño original. */
    public static byte[] cargarComoPNG(Path carpeta, String baseName) throws IOException {
        Path ruta = resolverArchivo(carpeta, baseName);
        if (ruta == null)
            throw new FileNotFoundException("No se encontró imagen para " + baseName + " en " + carpeta.toAbsolutePath());
        BufferedImage src = ImageIO.read(ruta.toFile());
        if (src == null)
            throw new IOException("El archivo no es una imagen válida: " + ruta);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(src, "png", baos);
            return baos.toByteArray();
        }
    }

    // ---------- helpers ----------
    private static Path resolverArchivo(Path carpeta, String baseName) {
        for (String ext : EXTENSIONES) {
            Path p = carpeta.resolve(baseName + "." + ext);
            if (Files.exists(p)) return p;
        }
        return null;
    }

    private static BufferedImage escalar(BufferedImage src, int boxW, int boxH, boolean permitirUpscale) {
        int w = src.getWidth(), h = src.getHeight();

        double scale = Math.min((double) boxW / w, (double) boxH / h);
        if (!permitirUpscale) scale = Math.min(scale, 1.0); // evita agrandar imágenes chicas

        int nw = Math.max(1, (int) Math.round(w * scale));
        int nh = Math.max(1, (int) Math.round(h * scale));

        BufferedImage out = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,      RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, nw, nh, null);
        g2.dispose();
        return out;
    }
}

//cargo
//this.altaPerfilProponente("", "", "", "",imagen, "", "", null);

public void cargoUsuarios(){
java.nio.file.Path carpeta = java.nio.file.Path.of("imagenes usuarios");
 try{
     byte[] imagen;
     
     imagen = Fotos.cargarComoPNG(carpeta, "hr", 150, 150, true);
     this.altaPerfilProponente("hrubino", "Horacio", "Rubino", "horacio.rubino@guambia.com.uy",imagen, "18 de Julio 1234", "Horacio Rubino Torres nace el 25 de febrero de 1962, es conductor, actor y libretista. Debuta en 1982 en carnaval\n" + "en Los \"Klaper´s\", donde estuvo cuatro años, actuando y libretando. Luego para \"Gaby´s\" (6 años), escribió en\n" + "categoría revistas y humoristas y desde el comienzo y hasta el presente en su propio conjunto Momosapiens.", "https://twitter.com/horaciorubino");
     
     imagen = Fotos.cargarComoPNG(carpeta, "mb", 150, 150, true);
     this.altaPerfilProponente("mbusca", "Martín", "Buscaglia", "Martin.bus@agadu.org.uy",imagen, "Colonia 4321", "Martín Buscaglia (Montevideo, 1972) es un artista, músico, compositor y productor uruguayo. Tanto con su banda\n" + "(“Los Bochamakers”) como en su formato “Hombre orquesta”, o solo con su guitarra, ha recorrido el mundo\n" + "tocando entre otros países en España, Estados Unidos, Inglaterra, Francia, Australia, Brasil, Colombia, Argentina,\n" + "Chile, Paraguay, México y Uruguay. (Actualmente los Bochamakers son Matías Rada, Martín Ibarburu, Mateo\n" + "Moreno, Herman Klang) Paralelamente, tiene proyectos a dúo con el español Kiko Veneno, la cubana Yusa, el\n" + "argentino Lisandro Aristimuño, su compatriota Antolín, y a trío junto a los brasileros Os Mulheres Negras", "http://www.martinbuscaglia.com/");
     
     imagen = Fotos.cargarComoPNG(carpeta, "hg", 150, 150, true);
     this.altaPerfilProponente("hectorg", "Héctor", "Guido", "hector.gui@elgalpon.org.uy",imagen, "Gral. Flores 5645", "En 1972 ingresó a la Escuela de Arte Dramático del teatro El Galpón. Participó en más de treinta obras teatrales y\n" + "varios largometrajes. Integró el elenco estable de Radioteatro del Sodre, y en 2006 fue asesor de su Consejo\n" + "Directivo. Como actor recibió múltiples reconocimientos: cuatro premios Florencio, premio al mejor actor\n" + "extranjero del Festival de Miami y premio Mejor Actor de Cine 2008. Durante varios períodos fue directivo del\n" + "teatro El Galpón y dirigente de la Sociedad Uruguaya de Actores (SUA); integró también la Federación Uruguaya\n" + "de Teatros Independientes (FUTI). Formó parte del equipo de gestión de la refacción de los teatros La Máscara,\n" + "Astral y El Galpón, y del equipo de gestión en la construcción del teatro De la Candela y de la sala Atahualpa de El\n" + "Galpón.", null);

     imagen = Fotos.cargarComoPNG(carpeta, "tc", 150, 150, true);
     this.altaPerfilProponente("tabarec", "Tabaré", "Cardozo", "tabare.car@agadu.org.uy",imagen, "Santiago Rivas 1212", "Tabaré Cardozo (Montevideo, 24 de julio de 1971) es un cantante, compositor y murguista uruguayo; conocido por\n" + "su participación en la murga Agarrate Catalina, conjunto que fundó junto a su hermano Yamandú y Carlos\n" + "Tanco en el año 2001.", "https://www.facebook.com/Tabar%C3%A9-\nCardozo-55179094281/?ref=br_rs");

     imagen = Fotos.cargarComoPNG(carpeta, "cs", 150, 150, true);
     this.altaPerfilProponente("cachilas", "Waldemar “Cachila”", "Silva", "Cachila.sil@c1080.org.uy",imagen, "Br. Artigas 4567", "Nace en el año 1947 en el conventillo \"Medio Mundo\" ubicado en pleno Barrio Sur. Es heredero parcialmentejunto al resto de sus hermanos- de la Comparsa \"Morenada\" (inactiva desde el fallecimiento de Juan Ángel Silva),\n" + "en 1999 forma su propia Comparsa de negros y lubolos \"Cuareim 1080\". Director responsable, compositor y\n" + "cantante de la misma.", "https://www.facebook.com/C1080?ref=br_rs");
     
     //imagen = Fotos.cargarComoPNG(carpeta, "jb", 150, 150, true);
     this.altaPerfilProponente("juliob", "Julio", "Bocca", "juliobocca@sodre.com.uy J",null, "Benito Blanco 4321", null, null);
     
     //imagen = Fotos.cargarComoPNG(carpeta, "dp", 150, 150, true);
     this.altaPerfilProponente("diegop", "Diego", "Parodi", "diego@efectocine.com",null, "Emilio Frugoni 1138 Ap. 02", null, "http://www.efectocine.com");
     
     imagen = Fotos.cargarComoPNG(carpeta, "kh", 150, 150, true);
     this.altaPerfilProponente("kairoh", "Kairo", "Herrera", "kairoher@pilsenrock.com.uy",imagen, "Paraguay 1423", null, null);
     
     imagen = Fotos.cargarComoPNG(carpeta, "lb", 150, 150, true);
     this.altaPerfilProponente("losBardo", "Los", "Bardo", "losbardo@bardocientifico.com",imagen, "8 de Octubre 1429", "Queremos ser vistos y reconocidos como una organización: referente en divulgación científica con un fuerte\n" + "espíritu didáctico y divertido, a través de acciones coordinadas con otros divulgadores científicos, que permitan\n" + "establecer puentes de comunicación. Impulsora en la generación de espacios de democratización y apropiación\n" + "social del conocimiento científico.","https://bardocientifico.com/");
     
     //imagen = Fotos.cargarComoPNG(carpeta, "rh", 150, 150, true);
     this.altaPerfilColaborador("robinh", "Robin", "Henderson", "Robin.h@tinglesa.com.uy", null);
     
     imagen = Fotos.cargarComoPNG(carpeta, "mt", 150, 150, true);
     this.altaPerfilColaborador("marcelot", "Marcelo", "Tinelli", "marcelot@ideasdelsur.com.ar", imagen);
     
     imagen = Fotos.cargarComoPNG(carpeta, "en", 150, 150, true);
     this.altaPerfilColaborador("novick", "Edgardo", "Novick", "edgardo@novick.com.uy", imagen);
     
     imagen = Fotos.cargarComoPNG(carpeta, "sp", 150, 150, true);
     this.altaPerfilColaborador("sergiop", "Sergio", "Puglia", "puglia@alpanpan.com.uy", imagen);
     
     //imagen = Fotos.cargarComoPNG(carpeta, "ar", 150, 150, true);
     this.altaPerfilColaborador("chino", "Alvaro", "Recoba", "chino@trico.org.uy", null);
     
     imagen = Fotos.cargarComoPNG(carpeta, "ap", 150, 150, true);
     this.altaPerfilColaborador("tonyp", "Antonio", "Pacheco", "eltony@manya.org.uy", imagen);
     
     imagen = Fotos.cargarComoPNG(carpeta, "nj", 150, 150, true);
     this.altaPerfilColaborador("nicoJ", "Nicolás", "Jodal", "jodal@artech.com.uy", imagen);
     
     //imagen = Fotos.cargarComoPNG(carpeta, "jp", 150, 150, true);
     this.altaPerfilColaborador("juanP", "Juan", "Perez", "juanp@elpueblo.com", null);
     
     //imagen = Fotos.cargarComoPNG(carpeta, "mg", 150, 150, true);
     this.altaPerfilColaborador("Mengano", "Mengano", "Gómez", "menganog@elpueblo.com", null);
     
     //imagen = Fotos.cargarComoPNG(carpeta, "pl", 150, 150, true);
     this.altaPerfilColaborador("Perengano", "Perengano", "López", "pere@elpueblo.com", null);
     
     //imagen = Fotos.cargarComoPNG(carpeta, "tj", 150, 150, true);
     this.altaPerfilColaborador("Tiajaci", "Tía", "Jacinta", "jacinta@elpueblo.com", null);
     
    }catch (IOException e){
     javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar usuarios" + e.getMessage(),"Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

//this.seguirUsuario("", "");

public void cargoSeguidores(){
    
try{
this.seguirUsuario("hrubino", "hectorg");

this.seguirUsuario("hrubino", "diegop");

this.seguirUsuario("hrubino", "losBardo");

this.seguirUsuario("mbusca", "tabarec");

this.seguirUsuario("mbusca", "cachilas");

this.seguirUsuario("mbusca", "kairoh");

this.seguirUsuario("hectorg", "mbusca");

this.seguirUsuario("hectorg", "juliob");

this.seguirUsuario("tabarec", "hrubino");

this.seguirUsuario("tabarec", "cachilas");

this.seguirUsuario("cachilas", "hrubino");

this.seguirUsuario("juliob", "mbusca");

this.seguirUsuario("juliob", "diegop");

this.seguirUsuario("diegop", "hectorg");

this.seguirUsuario("diegop", "losBardo");

this.seguirUsuario("kairoh", "sergiop");

this.seguirUsuario("losBardo", "hrubino");

this.seguirUsuario("losBardo", "nicoJ");

this.seguirUsuario("robinh", "hectorg");

this.seguirUsuario("robinh", "juliob");

this.seguirUsuario("robinh", "diegop");

this.seguirUsuario("marcelot", "cachilas");

this.seguirUsuario("marcelot", "juliob");

this.seguirUsuario("marcelot", "kairoh");

this.seguirUsuario("novick", "hrubino");

this.seguirUsuario("novick", "tabarec");

this.seguirUsuario("novick", "cachilas");

this.seguirUsuario("sergiop", "mbusca");

this.seguirUsuario("sergiop", "juliob");

this.seguirUsuario("sergiop", "diegop");

this.seguirUsuario("chino", "tonyp");

this.seguirUsuario("tonyp", "chino");

this.seguirUsuario("nicoJ", "diegop");

this.seguirUsuario("nicoJ", "losBardo");

this.seguirUsuario("juanP", "tabarec");

this.seguirUsuario("juanP", "cachilas");

this.seguirUsuario("juanP", "kairoh");

this.seguirUsuario("Mengano", "hectorg");

this.seguirUsuario("Mengano", "juliob");

this.seguirUsuario("Mengano", "chino");

this.seguirUsuario("Perengano", "diegop");

this.seguirUsuario("Perengano", "tonyp");

this.seguirUsuario("Tiajaci", "juliob");

this.seguirUsuario("Tiajaci", "kairoh");

this.seguirUsuario("Tiajaci", "sergiop");
}catch (Exception e){
     javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar seguidores" + e.getMessage(),"Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

}

//this.altaCategoria("");
//this.altaSubCategoria("", "");

public void cargoTipos(){

try{
this.altaCategoria("Teatro");

this.altaCategoria("Literatura");

this.altaCategoria("Música");

this.altaCategoria("Cine");

this.altaCategoria("Danza");

this.altaCategoria("Carnaval");

this.altaSubCategoria("Teatro Dramático", "Teatro");

this.altaSubCategoria("Teatro Musica", "Teatro");

this.altaSubCategoria("Comedia", "Teatro");

this.altaSubCategoria("Stand-up ", "Comedia");

this.altaSubCategoria("Festival", "Música");

this.altaSubCategoria("Concierto", "Música");

this.altaSubCategoria("Cine al Aire Libre", "Cine");

this.altaSubCategoria("Cine a Pedal ", "Cine");

this.altaSubCategoria("Ballet", "Danza");

this.altaSubCategoria("Flamenco", "Danza");

this.altaSubCategoria("Murga", "Carnaval");

this.altaSubCategoria("Humoristas", "Carnaval");

this.altaSubCategoria("Parodistas", "Carnaval");

this.altaSubCategoria("Lubolos", "Carnaval");

this.altaSubCategoria("Revista", "Carnaval");
}catch (Exception e){
     javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar categorias" + e.getMessage(),"Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

}

public proponente buscoProponente(String nickname){

proponente seleccionado = null;
    for(proponente p : this.listarProponentes()){
    if(p.getNickname().equalsIgnoreCase(nickname)){
    seleccionado = p;
    break;
    }
    }
    return seleccionado;
}

public colaborador buscoColaborador(String nickname){

colaborador seleccionado = null;
    for(colaborador c : this.listarColaboradores()){
    if(c.getNickname().equalsIgnoreCase(nickname)){
    seleccionado = c;
    break;
    }
    }
    return seleccionado;
}

public Date doyFecha(int d, int m, int a){

    int dia = d;
    int mes = m;
    int anio = a;
    Calendar cal = Calendar.getInstance();
    cal.set(anio, mes - 1, dia);
    Date fecha = cal.getTime();
    return fecha;
    
}

public void cargoPropuestas(){
    
java.nio.file.Path carpeta = java.nio.file.Path.of("imagenes propuestas");
 
try{
    
byte[] imagen;

proponente seleccionado = this.buscoProponente("diegop");
Date fecha = this.doyFecha(16, 9, 2017);
this.altaPropuesta(seleccionado, "Cine en el Botánico", "El 16 de Diciembre a la hora 20 se proyectará la película \"Clever\", en el Jardín Botánico (Av. 19 de Abril 1181) en el marco\n" + "de las actividades realizadas por el ciclo Cultura al Aire Libre. El largometraje uruguayo de ficción Clever es dirigido por\n" + "Federico Borgia y Guillermo Madeiro. Es apto para mayores de 15 años.", "Cine al Aire Libre", "Jardín Botánico", fecha, 200, 150000, "Porcentaje", null, estadoPropuesta.INGRESADA);

//busco la prop. la modifico agregando los historiales y luego la guardo.





imagen = Fotos.cargarComoPNG(carpeta, "mom", 150, 150, true); //NO HAY IMAGEN MOM ESTA MAL EL LINK
seleccionado = this.buscoProponente("hrubino");
fecha = this.doyFecha(7, 10, 2017);
this.altaPropuesta(seleccionado, "Religiosamente", "MOMOSAPIENS presenta \"Religiosamente\". Mediante dos parodias y un hilo conductor que aborda la temática de la\n" + "religión Momosapiens, mediante el humor y la reflexión, hilvana una historia que muestra al hombre inmerso en el tema\n" + "religioso. El libreto está escrito utilizando diferentes lenguajes de humor, dando una visión satírica y reflexiva desde\n" + "distintos puntos de vista, logrando mediante situaciones paródicas armar una propuesta plena de arte carnavalero.", "Parodistas", "Teatro de Verano", fecha, 300, 300000, "Ambas", null, estadoPropuesta.INGRESADA);

imagen = Fotos.cargarComoPNG(carpeta, "pim", 150, 150, true);
seleccionado = this.buscoProponente("mbusca");
fecha = this.doyFecha(19, 10, 2017);
this.altaPropuesta(seleccionado, "El Pimiento Indomable", "El Pimiento Indomable, formación compuesta por Kiko Veneno y el uruguayo Martín Buscaglia, presentará este 19 de\n" + "Octubre, su primer trabajo. Bajo un título homónimo al del grupo, es un disco que según los propios protagonistas “no se\n" + "parece al de ninguno de los dos por separado. Entre los títulos que se podrán escuchar se encuentran “Nadador salvador”,\n" + "“América es más grande”, “Pescaito Enroscado” o “La reina del placer”.", "Concierto", "Teatro Solís", fecha, 400, 400000, "Porcentaje", null, estadoPropuesta.INGRESADA);

imagen = Fotos.cargarComoPNG(carpeta, "pil", 150, 150, true);
seleccionado = this.buscoProponente("kairoh");
fecha = this.doyFecha(21, 10, 2017);
this.altaPropuesta(seleccionado, "Pilsen Rock", "La edición 2017 del Pilsen Rock se celebrará el 21 de Octubre en la Rural del Prado y contará con la participación de más\n" + "de 15 bandas nacionales. Quienes no puedan trasladarse al lugar, tendrán la posibilidad de disfrutar los shows a través de\n" + "Internet, así como entrevistas en vivo a los músicos una vez finalizados los conciertos.", "Festival", "Rural de Prado", fecha, 1000, 900000, "Ambas", null, estadoPropuesta.INGRESADA);

seleccionado = this.buscoProponente("juliob");
fecha = this.doyFecha(05, 11, 2017);
this.altaPropuesta(seleccionado, "Romeo y Julieta", "Romeo y Julieta de Kenneth MacMillan, uno de los ballets favoritos del director artístico Julio Bocca, se presentará\n" + "nuevamente el 5 de Noviembre en el Auditorio Nacional del Sodre. Basada en la obra homónima de William Shakespeare,\n" + "Romeo y Julieta es considerada la coreografía maestra del MacMillan. La producción de vestuario y escenografía se realizó\n" + "en los Talleres del Auditorio Adela Reta, sobre los diseños originales.", "Ballet", "Auditorio Nacional del Sodre", fecha, 800, 750000, "Porcentaje", null, estadoPropuesta.INGRESADA);

imagen = Fotos.cargarComoPNG(carpeta, "udj", 150, 150, true);
seleccionado = this.buscoProponente("tabarec");
fecha = this.doyFecha(16, 11, 2017);
this.altaPropuesta(seleccionado, "Un día de Julio", "La Catalina presenta el espectáculo \"Un Día de Julio\" en Landia. Un hombre misterioso y solitario vive encerrado entre las\n" + "cuatro paredes de su casa. Intenta, con sus teorías extravagantes, cambiar el mundo exterior que le resulta inhabitable. Un\n" + "día de Julio sucederá algo que cambiará su vida y la de su entorno para siempre.", "Murga", "Landia", fecha, 650, 300000, "Ambas", null, estadoPropuesta.INGRESADA);

seleccionado = this.buscoProponente("hectorg");
fecha = this.doyFecha(3, 12, 2017);
this.altaPropuesta(seleccionado, "El Lazarillo de Tormes", "Vuelve unas de las producciones de El Galpón más aclamadas de los últimos tiempos. Esta obra se ha presentado en\n" + "Miami, Nueva York, Washington, México, Guadalajara, Río de Janeiro y La Habana. En nuestro país, El Lazarillo de\n" + "Tormes fue nominado en los rubros mejor espectáculo y mejor dirección a los Premios Florencio 1995, obteniendo su\n" + "protagonista Héctor Guido el Florencio a Mejor actor de ese año.", "Teatro Dramático", "Teatro el Galpón", fecha, 350, 175000, "Entrada", null, estadoPropuesta.INGRESADA);

seleccionado = this.buscoProponente("losBardo");
fecha = this.doyFecha(10, 12, 2017);
this.altaPropuesta(seleccionado, "Bardo en la FING", "El 10 de Diciembre se presentará Bardo Científico en la FING. El humor puede ser usado como una herramienta\n" + "importante para el aprendizaje y la democratización de la ciencia, los monólogos científicos son una forma didáctica de\n" + "apropiación del conocimiento científico y contribuyen a que el público aprenda ciencia de forma amena. Los invitamos a\n" + "pasar un rato divertido, en un espacio en el cual aprenderán cosas de la ciencia que los sorprenderán. ¡Los esperamos!", "Stand-up", "Anfiteatro Edificio José Luis Massera", fecha, 200, 100000, "Entrada", null, estadoPropuesta.INGRESADA);

}catch (Exception e){
     javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar propuestas" + e.getMessage(),"Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }


}

public propuesta buscoPropuesta(String titulo){
    propuesta prop = null;
for(propuesta p : this.listarPropuestas()){
if(p.getTitulo().equalsIgnoreCase(titulo)){
prop = p;
break;
}
}
return prop;
}

public void cargoColaboraciones(){ //faltaria LA HORA.

    try{
    colaborador seleccionado;
    propuesta propuesta;
    float monto;
    Date fecha;
    
    seleccionado = this.buscoColaborador("novick");
    propuesta = this.buscoPropuesta("Cine en el Botánico");
    monto = 50000;
    fecha = this.doyFecha(20, 5, 2017);
    this.altaColaboracion2(seleccionado, propuesta, monto, "Porcentaje", fecha);
    
    seleccionado = this.buscoColaborador("robinh");
    propuesta = this.buscoPropuesta("Cine en el Botánico");
    monto = 50000;
    fecha = this.doyFecha(24, 5, 2017);
    this.altaColaboracion2(seleccionado, propuesta, monto, "Porcentaje", fecha);
    
    seleccionado = this.buscoColaborador("nicoJ");
    propuesta = this.buscoPropuesta("Cine en el Botánico");
    monto = 50000;
    fecha = this.doyFecha(30, 5, 2017);
    this.altaColaboracion2(seleccionado, propuesta, monto, "Porcentaje", fecha);
    
    }catch (Exception e){
     javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar colaboraciones" + e.getMessage(),"Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    
    
    
}









}




   