package com.example.gashistory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gashistory.databinding.ActivityGasolinaBinding;
import com.example.gashistory.model.Gasolina;

public class GasolinaActivity extends AppCompatActivity {

    private ActivityGasolinaBinding activityGasolinaBinding;
    private int posicao = -1;
    private Gasolina gasolina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGasolinaBinding = ActivityGasolinaBinding.inflate(getLayoutInflater());

        setContentView(activityGasolinaBinding.getRoot());

        activityGasolinaBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    gasolina = new Gasolina(
                            activityGasolinaBinding.dataEt.getText().toString(),
                            activityGasolinaBinding.precoEt.getText().toString()
                    );

                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_GASOLINA, gasolina);

                    //Se foi edição, devolver posição também
                    if(posicao!=-1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }

                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        //Verificando se é uma edição ou consulta e preenchimento de campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        gasolina = getIntent().getParcelableExtra(MainActivity.EXTRA_GASOLINA);
        if(gasolina!=null){
            activityGasolinaBinding.dataEt.setEnabled(false);
            activityGasolinaBinding.dataEt.setText(gasolina.getData());
            activityGasolinaBinding.precoEt.setText(gasolina.getPreco());


            if(posicao==-1){
                for (int i = 0; i < activityGasolinaBinding.getRoot().getChildCount(); i++){
                    activityGasolinaBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityGasolinaBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}
