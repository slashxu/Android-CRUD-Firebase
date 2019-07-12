package com.soliwork.gamestation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soliwork.gamestation.Model.Produtos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dataRef;

    EditText editName, editCategory;
    ListView listV_dados;

    private List<Produtos> listProdutos = new ArrayList<Produtos>();
    private ArrayAdapter<Produtos> arrayAdapterProdutos;

    Produtos produtoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editCategory = findViewById(R.id.editCategory);
        listV_dados = findViewById(R.id.listV_dados);

        inicializeDatabase();

        eventoDatabase();

        listV_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                produtoSelecionado = (Produtos) parent.getItemAtPosition(position);
                editName.setText(produtoSelecionado.getNameProduto());
                editCategory.setText(produtoSelecionado.getNameCategoria());
            }
        });
    }

        private void eventoDatabase() {
        dataRef.child("Produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listProdutos.clear();

                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    Produtos prod = objSnap.getValue(Produtos.class);
                    listProdutos.add(prod);
                }
                arrayAdapterProdutos = new ArrayAdapter<Produtos>(MainActivity.this, android.R.layout.simple_list_item_1, listProdutos);

                listV_dados.setAdapter(arrayAdapterProdutos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Inicializa o banco de dados (Firebase)
    private void inicializeDatabase() {
        FirebaseApp.initializeApp(MainActivity.this);
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        dataRef = database.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_novo) {
            Produtos prod = new Produtos();
            prod.setUid(UUID.randomUUID().toString());
            prod.setNameProduto(editName.getText().toString());
            prod.setNameCategoria(editCategory.getText().toString());
            // Cria a tabela Produtos e seta chave primaria no id
            dataRef.child("Produtos").child(prod.getUid()).setValue(prod);
            limparCampos();
        }else if (id == R.id.menu_update){
            Produtos prod = new Produtos();
            prod.setUid(produtoSelecionado.getUid());
            prod.setNameProduto(editName.getText().toString().trim());
            prod.setNameCategoria(editCategory.getText().toString().trim());
            // Cria a tabela Produtos e seta chave primaria no id
            dataRef.child("Produtos").child(prod.getUid()).setValue(prod);
            limparCampos();
        }else if (id == R.id.menu_delete){
            Produtos prod = new Produtos();
            prod.setUid(produtoSelecionado.getUid());
            dataRef.child("Produtos").child(prod.getUid()).removeValue();
            limparCampos();
        }

        return true;
    }

    private void limparCampos() {
        editName.setText("");
        editCategory.setText("");
    }
}
