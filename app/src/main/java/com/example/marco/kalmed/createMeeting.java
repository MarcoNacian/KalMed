package com.example.marco.kalmed;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class createMeeting extends AppCompatActivity implements View.OnClickListener {

    private  static final  String CERO = "0";
    private  static  final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    @BindView(R.id.nomPacienteET) EditText nombrePacienteET;
    @BindView(R.id.telPacienteET) EditText telefonoPacienteET;
    @BindView(R.id.mailPacienteET) EditText mailPacienteET;
    @BindView(R.id.fechaConsultaET) EditText fechaConsultaET;
    @BindView(R.id.hraConsultaET) EditText horaConsultaET;

    public final Calendar calendario = Calendar.getInstance();

    final int mes  = calendario.get(Calendar.MONTH);
    final int dia  = calendario.get(Calendar.DAY_OF_MONTH);
    final int anio  = calendario.get(Calendar.YEAR);
    final int hora = calendario.get(Calendar.HOUR_OF_DAY);
    final int minuto = calendario.get(Calendar.MINUTE);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        ButterKnife.bind(this);

        fechaConsultaET.setOnClickListener(this);
        horaConsultaET.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fechaConsultaET:obtenerFecha();
            break;
        }

        switch (v.getId()){
            case R.id.hraConsultaET:obtenerHora();
            break;
        }

    }



    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fechaConsultaET.setText(diaFormateado + BARRA + mesFormateado+BARRA+year);
            }

        },anio,mes,dia);
        recogerFecha.show();
    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                horaConsultaET.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        },hora,minuto, false);
        recogerHora.show();

    }
}
