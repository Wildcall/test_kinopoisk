package ru.malygin.parser.dao.impl;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.malygin.parser.dao.FilmDao;
import ru.malygin.parser.entity.Film;
import ru.malygin.parser.util.HibernateSessionFactoryUtil;

import java.util.List;

@Transactional
public class FilmDaoImpl implements FilmDao {

    @Override
    public void saveAll(List<Film> films) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Transaction tr = session.beginTransaction();

        films.forEach(session::persist);
        tr.commit();
        session.close();
    }
}
