package logica;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.categoria;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-15T21:23:10", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(categoria.class)
public class categoria_ { 

    public static volatile SingularAttribute<categoria, categoria> padre;
    public static volatile ListAttribute<categoria, categoria> hijos;
    public static volatile SingularAttribute<categoria, Integer> id;
    public static volatile SingularAttribute<categoria, String> nombre;

}