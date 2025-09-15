package logica;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.colaboracion;
import logica.estadoPropuesta;
import logica.historialEstado;
import logica.proponente;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-14T21:50:37", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(propuesta.class)
public class propuesta_ { 

    public static volatile SingularAttribute<propuesta, String> descripcion;
    public static volatile SingularAttribute<propuesta, String> lugar;
    public static volatile SingularAttribute<propuesta, Float> montoNecesario;
    public static volatile SingularAttribute<propuesta, String> titulo;
    public static volatile SingularAttribute<propuesta, byte[]> imagen;
    public static volatile SingularAttribute<propuesta, String> tipoEspectaculo;
    public static volatile SingularAttribute<propuesta, Float> precioEntrada;
    public static volatile SingularAttribute<propuesta, estadoPropuesta> estadoActual;
    public static volatile SingularAttribute<propuesta, Date> fechaRealizacion;
    public static volatile SingularAttribute<propuesta, String> tipoRetorno;
    public static volatile ListAttribute<propuesta, colaboracion> colaboraciones;
    public static volatile SingularAttribute<propuesta, proponente> prop;
    public static volatile SingularAttribute<propuesta, Date> fechaPublicada;
    public static volatile ListAttribute<propuesta, historialEstado> historial;
    public static volatile SingularAttribute<propuesta, Float> montoRecaudado;

}