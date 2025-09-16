package logica;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.colaborador;
import logica.propuesta;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-15T21:23:10", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(colaboracion.class)
public class colaboracion_ { 

    public static volatile SingularAttribute<colaboracion, colaborador> colaborador;
    public static volatile SingularAttribute<colaboracion, Date> fecha;
    public static volatile SingularAttribute<colaboracion, String> tipoRetorno;
    public static volatile SingularAttribute<colaboracion, propuesta> propuesta;
    public static volatile SingularAttribute<colaboracion, Float> montoAportado;
    public static volatile SingularAttribute<colaboracion, Integer> id;

}