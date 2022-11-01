package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.List;

@Service
public class MPAService {

    private final MPAStorage mpaStorage;

    @Autowired
    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MPA getMPAByID(int id) {
        if (mpaStorage.getMPAByID(id) != null) {
            return mpaStorage.getMPAByID(id);
        } else {
            throw new ObjectNotFoundException("Запись не найдена");
        }
    }

    public List<MPA> getAllMPA() {
        return mpaStorage.getAllMPA();
    }
}
