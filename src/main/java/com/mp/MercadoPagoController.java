package com.mp;

import com.google.gson.GsonBuilder;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.MerchantOrder;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MercadoPagoController {

    /*
    Sandbox Credit Card:

    State: APROVED
    Type: Mastercard
    Number:	5031755734530604
    CVV: 123
    Expire at: 11/25
    Holder: APRO GOMEZ
    DNI: 31256588
    Email: apro_gomez@gmail.com
    ---------------------------------
    State: REJECTED
    Type: Mastercard
    Number:	5031755734530604
    CVV: 123
    Expire at: 11/25
    Holder: EXPI GOMEZ
    DNI: 31256588
    Email: expi_gomez@gmail.com
     */

    @GetMapping("/createAndRedirect")
    public String createAndRecirect() throws MPException {
        Preference preference = new Preference();


        preference.setBackUrls(
           new BackUrls().setFailure("http://localhost:8080/failure")
           .setPending("http://localhost:8080/pending")
           .setSuccess("http://localhost:8080/success")
        );

        Item item = new Item();
        item.setTitle("Test Item")
                .setQuantity(1)
                .setUnitPrice((float) 75.56);
        preference.appendItem(item);

        var result = preference.save();

        System.out.println(result.getSandboxInitPoint());
        return "redirect:" + result.getSandboxInitPoint();
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request,
                          @RequestParam("collection_id") String collectionId,
                          @RequestParam("collection_status") String collectionStatus,
                          @RequestParam("external_reference") String externalReference,
                          @RequestParam("payment_type") String paymentType,
                          @RequestParam("merchant_order_id") String merchantOrderId,
                          @RequestParam("preference_id") String preferenceId,
                          @RequestParam("site_id") String siteId,
                          @RequestParam("processing_mode") String processingMode,
                          @RequestParam("merchant_account_id") String merchantAccountId,
                          Model model
    ) throws MPException {
        var payment = com.mercadopago.resources.Payment.findById(collectionId);

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(payment));

        model.addAttribute("payment", payment);
        return "ok";
    }

    @GetMapping("/failure")
    public String failure(HttpServletRequest request,
                          @RequestParam("collection_id") String collectionId,
                          @RequestParam("collection_status") String collectionStatus,
                          @RequestParam("external_reference") String externalReference,
                          @RequestParam("payment_type") String paymentType,
                          @RequestParam("merchant_order_id") String merchantOrderId,
                          @RequestParam("preference_id") String preferenceId,
                          @RequestParam("site_id") String siteId,
                          @RequestParam("processing_mode") String processingMode,
                          @RequestParam("merchant_account_id") String merchantAccountId,
                          Model model
                          ) throws MPException {
            model.addAttribute("preference_id", preferenceId);

            var preference = Preference.findById(preferenceId);
            var order = MerchantOrder.findById(merchantOrderId);
            var payment = com.mercadopago.resources.Payment.findById(collectionId);

            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(order));
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(payment));

            model.addAttribute("items", preference.getItems());
            model.addAttribute("payment", payment);
        return "failure";
    }

    @GetMapping("/pending")
    public String pending(HttpServletRequest request,
                          @RequestParam("collection_id") String collectionId,
                          @RequestParam("collection_status") String collectionStatus,
                          @RequestParam("external_reference") String externalReference,
                          @RequestParam("payment_type") String paymentType,
                          @RequestParam("merchant_order_id") String merchantOrderId,
                          @RequestParam("preference_id") String preferenceId,
                          @RequestParam("site_id") String siteId,
                          @RequestParam("processing_mode") String processingMode,
                          @RequestParam("merchant_account_id") String merchantAccountId,
                          Model model
    ) throws MPException {
        //Not implemented yet
        return "pending";
    }
}
