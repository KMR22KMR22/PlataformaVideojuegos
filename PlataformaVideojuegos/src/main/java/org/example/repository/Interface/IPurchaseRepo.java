package org.example.repository.Interface;


import org.example.model.entidad.PurchaseEntity;
import org.example.model.form.PurchaseForm;
import org.example.model.form.updates.PurchaseUpdate;

public interface IPurchaseRepo extends  ICrud<PurchaseEntity, PurchaseForm, PurchaseUpdate, Long>{
}
