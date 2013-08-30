package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;

import java.util.List;

public class ServiceFacadeClient {
    public static void main(String[] args) {
        try {
            final ServiceFacade serviceFacade = new ServiceFacade();
            for (ConfirmationRegister confirmationregister :
                 (List<ConfirmationRegister>) serviceFacade.getFindConfirmationRegisterById(1) /* FIXME: Pass parameters here */) {
                printConfirmationRegister(confirmationregister);
            }
            for (BaptismalRegister baptismalregister :
                 (List<BaptismalRegister>) serviceFacade.getFindBaptismalRegisterById(1) /* FIXME: Pass parameters here */) {
                printBaptismalRegister(baptismalregister);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void printConfirmationRegister(ConfirmationRegister confirmationregister) {
        System.out.println("registerId = " + confirmationregister.getRegisterId());
        System.out.println("line = " + confirmationregister.getLine());
        System.out.println("volume = " + confirmationregister.getVolume());
        System.out.println("firstName = " + confirmationregister.getFirstName());
        System.out.println("lastName = " + confirmationregister.getLastName());
        System.out.println("middleName = " + confirmationregister.getMiddleName());
    }

    private static void printBaptismalRegister(BaptismalRegister baptismalregister) {
        System.out.println("age = " + baptismalregister.getAge());
        System.out.println("baptismalDate = " + baptismalregister.getBaptismalDate());
        System.out.println("birthDate = " + baptismalregister.getBirthDate());
        System.out.println("birthPlace = " + baptismalregister.getBirthPlace());
        System.out.println("father = " + baptismalregister.getFather());
        System.out.println("fatherBirthPlace = " + baptismalregister.getFatherBirthPlace());
        System.out.println("firstName = " + baptismalregister.getFirstName());
        System.out.println("lastName = " + baptismalregister.getLastName());
        System.out.println("legitimacy = " + baptismalregister.getLegitimacy());
        System.out.println("line = " + baptismalregister.getLine());
        System.out.println("middleName = " + baptismalregister.getMiddleName());
        System.out.println("minister = " + baptismalregister.getMinister());
        System.out.println("mother = " + baptismalregister.getMother());
        System.out.println("motherBirthPlace = " + baptismalregister.getMotherBirthPlace());
        System.out.println("page = " + baptismalregister.getPage());
        System.out.println("registerId = " + baptismalregister.getRegisterId());
        System.out.println("remarks = " + baptismalregister.getRemarks());
        System.out.println("suffix = " + baptismalregister.getSuffix());
        System.out.println("volume = " + baptismalregister.getVolume());
    }
}
