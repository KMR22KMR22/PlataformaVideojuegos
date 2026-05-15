package org.example;

import HibernateU.HibernateUtil;
import org.example.controller.PurchaseController;
import org.example.controller.UserController;
import org.example.controller.gameController.GameController;
import org.example.controller.libraryController.LibraryController;
import org.example.controller.reviewController.ReviewController;
import org.example.exeptions.ValidationException;
import org.example.model.dto.game.GameAgeClasification;
import org.example.model.entidad.GameEntity;
import org.example.model.entidad.ReviewEntity;
import org.example.model.form.GameForm;
import org.example.model.form.UserForm;
import org.example.model.paymentMethod.PaymentMethod;
import org.example.repository.inMemory.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        var session = HibernateUtil.getSessionFactory().openSession();
        session.close();

    }
}