package ru.malygin.parser.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.malygin.parser.dao.FilmDao;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.util.HibernateSessionFactoryUtil;

public class FilmDaoImpl implements FilmDao {
    @Override
    public void save(Film film) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Transaction tr = session.beginTransaction();
        session.persist(film);
        tr.commit();
        session.close();
    }
}
