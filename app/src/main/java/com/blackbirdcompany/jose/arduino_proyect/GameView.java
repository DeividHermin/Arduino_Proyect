package com.blackbirdcompany.jose.arduino_proyect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


/**
 * Created by jose on 09/02/2017.
 */

public class GameView extends SurfaceView {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private int RADIO, EJEX, EJEY;
    private static final int RASTRO = 30; //numero de lineas que habrá de rastro
    float lineas[][];
    int degra[];
    //ArrayList posiciones;

    public GameView(final Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

        ang = 30;
        bolAux=true;

        lineas = new float[RASTRO][3];
        for(int i = 0; i<RASTRO; i++) {
            for (int n = 0; n<3; n++){
                lineas[i][n] = 0;
            }
        }
        degra = new int[RASTRO];
        int aux = 250;
        int resta = aux/RASTRO;
        for(int i = 0; i<degra.length; i++) {
            degra[i] = aux;
            aux -= resta;
        }
        distancia = -1;
    }

    //Variables para probar
    float distancia;
    int ang;
    boolean bolAux;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onDraw(Canvas canvas) {

        //Define el radio de la circunferencia exterior
        RADIO = getWidth()/2 - 30;
        EJEX = getWidth()/2;
        EJEY = getHeight();

        //Vuelve a dibujar la base del canvas
        Paint pincel=new Paint();
        basePrincipal(canvas,pincel);

        //Dibuja las lineas del radar
        pincel.setStrokeWidth(8);

        dibujaLinea(canvas, pincel, ang, distancia);

        //simulacion de objeto
        switch (ang){
            case 50: distancia=300;break;
            case 51: distancia=315;break;
            case 52: distancia=330;break;
            case 53: distancia=345;break;
            case 54: distancia=360;break;
            case 55: distancia=365;break;
            case 56: distancia=367;break;
            case 57: distancia=368;break;
            case 58: distancia=367;break;
            case 59: distancia=365;break;
            case 60: distancia=360;break;
            case 61: distancia=345;break;
            case 62: distancia=330;break;
            case 63: distancia=315;break;
            case 64: distancia=565;break;
            case 65: distancia=568;break;
            case 66: distancia=572;break;
            case 67: distancia=568;break;
            case 68: distancia=565;break;
            case 69: distancia=315;break;
            case 70: distancia=330;break;
            case 71: distancia=345;break;
            case 72: distancia=360;break;
            case 73: distancia=365;break;
            case 74: distancia=367;break;
            case 75: distancia=368;break;
            case 76: distancia=367;break;
            case 77: distancia=365;break;
            case 78: distancia=360;break;
            case 79: distancia=345;break;
            case 80: distancia=330;break;
            case 81: distancia=315;break;
            case 82: distancia=300;break;
            default: distancia=-1 ;break;
        }

        if(bolAux) {
            ang++;
        }else {
            ang--;
        }
        if(ang>=150 || ang<=30)
            bolAux=!bolAux;

    }

    private void basePrincipal(Canvas canvas,Paint pincel){

        //Dibuja fondo
        pincel.setColor(Color.BLACK);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), pincel);

        //Prepara el pincel
        pincel.setStrokeWidth(5);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setColor(Color.GREEN);

        //Dibuja las 5 lineas de los grados (30, 60, 90, 120, 150)
        canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(30))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(30))*(50 + RADIO))),pincel);
        canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(60))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(60))*(50 + RADIO))),pincel);
        canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(90))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(90))*(50 + RADIO))),pincel);
        canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(120))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(120))*(50 + RADIO))),pincel);
        canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(150))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(150))*(50 + RADIO))),pincel);

        //Dibuja linea inferior
        canvas.drawLine(0,getHeight()-3,getWidth(),getHeight()-3,pincel);

        //Dibuja los circulos
        pincel.setStrokeWidth(5);
        canvas.drawCircle(EJEX,EJEY,RADIO-600,pincel);
        canvas.drawCircle(EJEX,EJEY,RADIO-400,pincel);
        canvas.drawCircle(EJEX,EJEY,RADIO-200,pincel);
        canvas.drawCircle(EJEX,EJEY,RADIO,pincel);

        //Dibuja los numeros alrrededor de la circunferencia
        Path trazado = new Path();
        trazado.addCircle(EJEX, EJEY, 70+RADIO, Path.Direction.CW);
        pincel.setStrokeWidth(5);
        pincel.setStyle(Paint.Style.FILL);
        pincel.setTextSize(40);
        pincel.setTypeface(Typeface.SANS_SERIF);
        float aux = (float)(2*Math.PI*(70 + RADIO))/360; //longitud de la circunferencia (2*PI*RADIO) por cada grado
        canvas.drawTextOnPath("30º", trazado, (330-1)*aux,0, pincel);
        canvas.drawTextOnPath("60º", trazado, (300-1)*aux,0, pincel);
        canvas.drawTextOnPath("90º", trazado, (270-1)*aux,0, pincel);
        canvas.drawTextOnPath("120º", trazado, (240-2)*aux,0, pincel);
        canvas.drawTextOnPath("150º", trazado, (210-2)*aux,0, pincel);
    }

    private void dibujaLinea(Canvas canvas, Paint pincel, int angulo, float distancia){
        //Dibuja una linea, dibuja el rastro y la guarda en el array de lineas rastro
        pincel.setARGB(255, 255, 117, 20);
        canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(angulo))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(angulo))*(50 + RADIO))), pincel);
        dibujaRastro(canvas, pincel);
        guardaLinea(angulo, distancia);
    }


    private void guardaLinea(int angulo, float distancia){
        //Mueve los objetos del array una posicion hacia el final y guarda la nueva linea en la posicion 0. Y guarda la distancia del objeto detectado (-1 si no hay)
        for(int i = RASTRO-1; i>0; i--) {
                lineas[i][0] = lineas[i-1][0];
                lineas[i][1] = lineas[i-1][1];
        }
        lineas[0][0]=angulo;
        lineas[0][1]=distancia;
    }

    private void dibujaRastro(Canvas canvas, Paint pincel){
        //Dibuja todas las lineas guardadas en el array de lineas. Si se detectó un objeto (distancia>0) lo dibuja en rojo
        for(int i = 0; i<RASTRO; i++) {
            if (lineas[i][0] != 0) {
                pincel.setARGB(degra[i], 255, 117, 20);
                canvas.drawLine(EJEX, EJEY, (float)((getWidth()/2)-(Math.cos(Math.toRadians(lineas[i][0]))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(lineas[i][0]))*(50 + RADIO))), pincel);

                if(lineas[i][1]>0)
                    dibujaLineaOb(canvas, pincel, lineas[i][0], lineas[i][1], degra[i]);
            }

        }
    }

    private void dibujaLineaOb(Canvas canvas, Paint pincel, float angulo, float distancia, int degra){
        //Dibuja una linea desde en el angulo indicado desde la distancia indicada hasta el final
        pincel.setARGB(degra, 255, 0 ,0);
        canvas.drawLine((float)((getWidth()/2)-(Math.cos(Math.toRadians(angulo))*(distancia))), (float)((getHeight())-(Math.sin(Math.toRadians(angulo))*(distancia))), (float)((getWidth()/2)-(Math.cos(Math.toRadians(angulo))*(50 + RADIO))), (float)((getHeight())-(Math.sin(Math.toRadians(angulo))*(50 + RADIO))),pincel);
    }
}
