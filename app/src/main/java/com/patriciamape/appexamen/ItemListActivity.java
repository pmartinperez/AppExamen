package com.patriciamape.appexamen;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends AppCompatActivity
        implements ItemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
        //


    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        //Recogemos la variable definida en el config.xml
        boolean dual_pane = getResources().getBoolean(R.bool.dual_pane);
        // Al pulsar un item, si la variable tiene valor True, es decir, el dispositivo se encuentra en posicion landscape
        // mostramos un mensaje emergente
        if(dual_pane){
            Toast.makeText(ItemListActivity.this, "Tumbado", Toast.LENGTH_SHORT).show();
        }
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            //se inicia otra activity de la que se espera que devuelva un dato. El REQUEST CODE es 1.
            startActivityForResult(detailIntent, 1);
        }
    }

    // Metodo del boton Limpiar del fragment del detalle. Recogemos el textview del fragment detalle
    // y lo borramos
    public void botonLimpiar(View view){
        TextView textview1 = (TextView) findViewById(R.id.item_detail);
        if (textview1 != null)
            textview1.setText(" ");
    }

    //Cuando cerremos la activity del detalle se lanza este metodo. Diferenciamos los intents con la
    // variable REQUEST CODE que introducimos en el metodo startActivityForResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {//REQUEST CODE, RESULT_OK, dato enviado

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                String dato = intentData.getStringExtra("resultado");
                Toast.makeText(ItemListActivity.this, dato, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
