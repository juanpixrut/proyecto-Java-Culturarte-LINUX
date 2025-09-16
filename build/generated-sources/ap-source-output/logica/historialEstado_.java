package logica;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.estadoPropuesta;
import logica.propuesta;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-15T21:23:10", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(historialEstado.class)
public class historialEstado_ { 

    public static volatile SingularAttribute<historialEstado, Date> fecha;
    public static volatile SingularAttribute<historialEstado, estadoPropuesta> estado;
    public static volatile SingularAttribute<historialEstado, propuesta> propuesta;
    public static volatile SingularAttribute<historialEstado, Integer> id;

}