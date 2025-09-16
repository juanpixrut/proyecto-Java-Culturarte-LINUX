package logica;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.usuario;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-15T21:23:10", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(usuario.class)
public class usuario_ { 

    public static volatile SingularAttribute<usuario, Date> fechaNacimiento;
    public static volatile SingularAttribute<usuario, String> apellido;
    public static volatile SingularAttribute<usuario, String> correo;
    public static volatile SingularAttribute<usuario, String> nickname;
    public static volatile SingularAttribute<usuario, byte[]> imagen;
    public static volatile SingularAttribute<usuario, String> nombre;
    public static volatile ListAttribute<usuario, usuario> seguidores;
    public static volatile ListAttribute<usuario, usuario> seguidos;

}