/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.propuesta;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

import javax.persistence.Persistence;

/**
 *
 * @author Juanpi
 */
public class propuestaJpaController implements Serializable {

    public propuestaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public propuestaJpaController(){
    emf = Persistence.createEntityManagerFactory("culturartePU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(propuesta propuesta) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(propuesta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findpropuesta(propuesta.getTitulo()) != null) {
                throw new PreexistingEntityException("propuesta " + propuesta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(propuesta propuesta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            propuesta = em.merge(propuesta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = propuesta.getTitulo();
                if (findpropuesta(id) == null) {
                    throw new NonexistentEntityException("The propuesta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            propuesta propuesta;
            try {
                propuesta = em.getReference(propuesta.class, id);
                propuesta.getTitulo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propuesta with id " + id + " no longer exists.", enfe);
            }
            em.remove(propuesta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<propuesta> findpropuestaEntities() {
        return findpropuestaEntities(true, -1, -1);
    }

    public List<propuesta> findpropuestaEntities(int maxResults, int firstResult) {
        return findpropuestaEntities(false, maxResults, firstResult);
    }

    private List<propuesta> findpropuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(propuesta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public propuesta findpropuesta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(propuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getpropuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<propuesta> rt = cq.from(propuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
