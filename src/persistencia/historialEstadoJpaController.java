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
import logica.historialEstado;
import persistencia.exceptions.NonexistentEntityException;

import javax.persistence.Persistence;

/**
 *
 * @author Juanpi
 */
public class historialEstadoJpaController implements Serializable {

    public historialEstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public historialEstadoJpaController(){
    emf = Persistence.createEntityManagerFactory("culturartePU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(historialEstado historialEstado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(historialEstado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(historialEstado historialEstado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            historialEstado = em.merge(historialEstado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = historialEstado.getId();
                if (findhistorialEstado(id) == null) {
                    throw new NonexistentEntityException("The historialEstado with id " + id + " no longer exists.");
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
            historialEstado historialEstado;
            try {
                historialEstado = em.getReference(historialEstado.class, id);
                historialEstado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialEstado with id " + id + " no longer exists.", enfe);
            }
            em.remove(historialEstado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<historialEstado> findhistorialEstadoEntities() {
        return findhistorialEstadoEntities(true, -1, -1);
    }

    public List<historialEstado> findhistorialEstadoEntities(int maxResults, int firstResult) {
        return findhistorialEstadoEntities(false, maxResults, firstResult);
    }

    private List<historialEstado> findhistorialEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(historialEstado.class));
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

    public historialEstado findhistorialEstado(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(historialEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int gethistorialEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<historialEstado> rt = cq.from(historialEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
