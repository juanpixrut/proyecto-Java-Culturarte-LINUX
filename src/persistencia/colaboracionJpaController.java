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
import logica.colaboracion;
import persistencia.exceptions.NonexistentEntityException;

import javax.persistence.Persistence;

/**
 *
 * @author Juanpi
 */
public class colaboracionJpaController implements Serializable {

    public colaboracionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public colaboracionJpaController(){
    emf = Persistence.createEntityManagerFactory("culturartePU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(colaboracion colaboracion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(colaboracion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(colaboracion colaboracion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            colaboracion = em.merge(colaboracion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = colaboracion.getId();
                if (findcolaboracion(id) == null) {
                    throw new NonexistentEntityException("The colaboracion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            colaboracion colaboracion;
            try {
                colaboracion = em.getReference(colaboracion.class, id);
                colaboracion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The colaboracion with id " + id + " no longer exists.", enfe);
            }
            em.remove(colaboracion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<colaboracion> findcolaboracionEntities() {
        return findcolaboracionEntities(true, -1, -1);
    }

    public List<colaboracion> findcolaboracionEntities(int maxResults, int firstResult) {
        return findcolaboracionEntities(false, maxResults, firstResult);
    }

    private List<colaboracion> findcolaboracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(colaboracion.class));
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

    public colaboracion findcolaboracion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(colaboracion.class, id);
        } finally {
            em.close();
        }
    }

    public int getcolaboracionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<colaboracion> rt = cq.from(colaboracion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
