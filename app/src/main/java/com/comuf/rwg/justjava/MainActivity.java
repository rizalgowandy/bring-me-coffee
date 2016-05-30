package com.comuf.rwg.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    public int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, getString(R.string.error_order_one_hundred), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        display(quantity);
    }

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, getString(R.string.error_order_zero), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        display(quantity);
    }

    public void submitOrder(View view) {
        boolean hasWhippedCream = false, hasChocolate = false, emailValid = false;
        String stringName = "";
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream);
        if (whippedCream != null) hasWhippedCream = whippedCream.isChecked();
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate);
        if (chocolate != null) hasChocolate = chocolate.isChecked();
        EditText name = (EditText) findViewById(R.id.input_name);
        if (name != null) stringName = name.getText().toString();
        EditText email = (EditText) findViewById(R.id.input_email);
        if (email != null) emailValid = isValidEmail(email.getText());
        int totalPrice = calculatePrice(hasWhippedCream, hasChocolate);
        String subject = getString(R.string.create_order_for, stringName);
        if (emailValid) {
            String stringEmail = email.getText().toString();
            String priceMessage = createOrderSummary(totalPrice, hasWhippedCream, hasChocolate, stringName, stringEmail);
            composeEmail(stringEmail, priceMessage, subject);
        } else {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void composeEmail(String address, String message, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getString(R.string.mailto)));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public int calculatePrice(boolean whipCream, boolean chocolate) {
        int forWhippedCream = 0, forChocolate = 0;
        if (whipCream) forWhippedCream = 1;
        if (chocolate) forChocolate = 2;
        return (5 + forWhippedCream + forChocolate) * quantity;
    }

    public String createOrderSummary(int totalPrice, boolean whipCream, boolean chocolate, String name, String email) {
        String temporary = getString(R.string.create_order_name, name);
        temporary += "\n" + getString(R.string.create_order_email, email);
        temporary += "\n" + getString(R.string.create_order_whipped_cream, whipCream);
        temporary += "\n" + getString(R.string.create_order_chocolate, chocolate);
        temporary += "\n" + getString(R.string.create_order_quantity, quantity);
        temporary += "\n" + getString(R.string.create_order_total_price, totalPrice);
        temporary += "\n" + getString(R.string.thank_you);
        return temporary;
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        if (quantityTextView != null) quantityTextView.setText(String.valueOf(number));
    }
}