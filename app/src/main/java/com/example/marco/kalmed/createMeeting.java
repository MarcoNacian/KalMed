package com.example.marco.kalmed;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class createMeeting extends AppCompatActivity implements View.OnClickListener {

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    @BindView(R.id.nomPacienteET)
    EditText nombrePacienteET;
    @BindView(R.id.telPacienteET)
    EditText telefonoPacienteET;
    @BindView(R.id.mailPacienteET)
    EditText mailPacienteET;
    @BindView(R.id.fechaConsultaET)
    EditText fechaConsultaET;
    @BindView(R.id.hraConsultaET)
    EditText horaConsultaET;
    @BindView(R.id.CrearBTNXML)
    Button crearConsultaBTN;

    public final Calendar calendario = Calendar.getInstance();
    final int mes = calendario.get(Calendar.MONTH);
    final int dia = calendario.get(Calendar.DAY_OF_MONTH);
    final int anio = calendario.get(Calendar.YEAR);
    final int hora = calendario.get(Calendar.HOUR_OF_DAY);
    final int minuto = calendario.get(Calendar.MINUTE);

    long calID = 0;
    long startMillis = 0;
    long endMillis = 0;




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
        switch (v.getId()) {
            case R.id.fechaConsultaET:
                obtenerFecha();
                break;
        }

        switch (v.getId()) {
            case R.id.hraConsultaET:
                obtenerHora();
                break;
        }

    }

    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fechaConsultaET.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }

        }, anio, mes, dia);
        recogerFecha.show();
    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                horaConsultaET.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, minuto, false);
        recogerHora.show();
    }

    public void datosPaciente(View view) throws ParseException {
        String nombrePaciente = nombrePacienteET.getText().toString();
        String telefonoPaciente = telefonoPacienteET.getText().toString();
        String correoPaciente = mailPacienteET.getText().toString();
        String fechaConsulta = fechaConsultaET.getText().toString();
        String horaConsulta = horaConsultaET.getText().toString();

        Bundle bundle = getIntent().getExtras();
        Log.i("datos", nombrePaciente + " " + telefonoPaciente + " " + correoPaciente + " " + fechaConsulta + " " + horaConsulta);


        calID = bundle.getLong("idCalendario");

        Date fecha = new SimpleDateFormat("dd/mm/yyyy").parse(fechaConsulta);


//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(fecha.getYear(), fecha.getMonth(), fecha.getDay(),11,12,12);
//        startMillis = beginTime.getTimeInMillis();
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(fecha.getYear(), fecha.getMonth(), fecha.getDay(),12,12,12);
//        endMillis = endTime.getTimeInMillis();


//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
//                .putExtra(CalendarContract.Events.TITLE, "Yoga")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//        startActivity(intent);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, fecha.getTime());
        values.put(CalendarContract.Events.DTEND,fecha.getTime());
        values.put(CalendarContract.Events.TITLE, "Consulta de : "+ nombrePaciente);
        values.put(CalendarContract.Events.DESCRIPTION,
                "Paciente :"+ nombrePaciente +" telefono: " + telefonoPaciente + " correo: "+ correoPaciente );
        values.put(CalendarContract.Events.CALENDAR_ID, 8);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Log.i("pff",Long.toString(eventID));
    }
}