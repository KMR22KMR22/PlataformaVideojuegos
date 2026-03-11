package org.example.Repository.Interface;


import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Form.PurchaseForm;
import org.example.Model.Form.Updates.PurchaseUpdateForm;

public interface IPurchaseRepo extends  ICrud<PurchaseEntity, PurchaseForm, PurchaseUpdateForm, Long>{
}
