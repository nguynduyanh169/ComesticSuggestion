/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import java.util.List;

/**
 *
 * @author anhnd
 */
public interface IGenericDAO<T> {
    T create(T t);

    T update(T t);

    boolean delete(T t);

    List<T> getAll(String namedQuery);
}
