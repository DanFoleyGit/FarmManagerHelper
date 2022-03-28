package com.example.farmmanagerhelper;

import com.example.farmmanagerhelper.models.ProduceEstimatorProfile;
import com.example.farmmanagerhelper.models.ShippingCalculatorProfile;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToolServicesTest {

    @Test
    public void performShippingCalc() {
        ShippingCalculatorProfile profile = new ShippingCalculatorProfile(
                "TestProfile", "001", "45", "50");

        int quantity1 = 45;
        int quantity2 = 150;
        int quantity3 = 137;

        String result1 = ToolServices.performShippingCalc(quantity1,profile);
        String result2 = ToolServices.performShippingCalc(quantity2,profile);
        String result3 = ToolServices.performShippingCalc(quantity3,profile);

        assertEquals("1 pallets by 45 units.",result1);
        assertEquals("3 pallets by 45 units.\n\n1 pallet by 15.", result2);
        assertEquals("2 pallets by 45 units.\n\n1 pallet by 47.", result3);
    }

    @Test
    public void performProduceEstimation() {
        ProduceEstimatorProfile profile = new ProduceEstimatorProfile(
                "TestProfile", "001","12000","500","18","10");

        int quantity1 = 200;
        int quantity2 = 100;
        int quantity3 = 1;

        float result1 = ToolServices.performProduceEstimation(quantity1,profile);
        float result2 = ToolServices.performProduceEstimation(quantity2,profile);
        float result3 = ToolServices.performProduceEstimation(quantity3,profile);

        assertEquals(165,result1,0);
        assertEquals(82.5, result2,0);
        assertEquals(0.824999988079071, result3,0);

    }
}