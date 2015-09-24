package com.example.sscholtz.motorental;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.sscholtz.motorental.model.Customer;
import com.example.sscholtz.motorental.services.Impl.CustomerDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class view extends AppCompatActivity {

    private Dialog dialog;
    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;
    private List<Customer> customers;
    private ArrayList<String> strcustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(view.this, main_page.class);
        i.putExtra("Tab", "Doctor");
        finish();
        startActivity(i);
    }
   public void onClick(View view) {
       //Go back to Main Menu
       Intent openStep = new Intent(view.this, main_page.class);
       startActivity(openStep);
       finish();
   }


    private void viewCustomers() {
        final ListView list = (ListView) findViewById(R.id.viewCustomer);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CustomerDetailsServiceImpl service = new CustomerDetailsServiceImpl();
                customers = service.findAll();
                strcustomers = new ArrayList<String>();

                for (Customer customer : customers) {
                    strcustomers.add(customer.getId() + "  |  " + customer.getFirstName() + " " + customer.getLastName());
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.this, android.R.layout.simple_list_item_1, android.R.id.text1, strcustomers);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                dialog = new Dialog(view.this);

                dialog.setContentView(R.layout.activity_view);
                ScrollView l = (ScrollView) dialog.findViewById(R.id.scrollViewCustomerView);
                l.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                mDownX = event.getX();
                                mDownY = event.getY();
                                isOnClick = true;
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                if (isOnClick) {
                                    dialog.dismiss();
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {
                                    isOnClick = false;
                                }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                dialog.setTitle("Customer Details");
                dialog.show();
            }
        });
    }
}

























    ///////////////////////////////
  /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}*/
