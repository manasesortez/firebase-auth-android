package com.amtodev.hospitalReservations.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtodev.hospitalReservations.R;

import java.util.ArrayList;

public class listaAdaptadorVehiculo extends RecyclerView.Adapter<listaAdaptadorVehiculo.DataViewHolder> {
    Context contexto;
    ArrayList<Adaptador> arregloVehiculos = new ArrayList<>();
    LayoutInflater inflaterAdaptador;
    Adaptador objVehiculoModelo;

    public listaAdaptadorVehiculo(Context contexto, ArrayList<Adaptador> arregloVehiculos){
        this.contexto = contexto;
        this.arregloVehiculos = arregloVehiculos;
    }

    @NonNull
    @Override
    public listaAdaptadorVehiculo.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewdoctor_info, parent, false);
        return new listaAdaptadorVehiculo.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull listaAdaptadorVehiculo.DataViewHolder holder, int position) {
        holder.viewBind(arregloVehiculos.get(position), position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return arregloVehiculos.size();
    }

    public class DataViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView CarModel, CarMark, CarYear, TaskOption;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            CarModel = itemView.findViewById(R.id.CarModel);
            CarMark = itemView.findViewById(R.id.CarMark);
            CarYear = itemView.findViewById(R.id.CarYear);
            TaskOption = itemView.findViewById(R.id.txtOption);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
        public void viewBind(Adaptador adaptador, int position) {
            CarModel.setText("Modelo: " + adaptador.getModelo());
            CarMark.setText("Marca: " + adaptador.getMarca());
            CarYear.setText("A??o: " + adaptador.getAnio());

            TaskOption.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(contexto, TaskOption);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.menu_edit:
                            Intent intent = new Intent(contexto, UpdateCar.class);
                            intent.putExtra("edit", (Parcelable) adaptador);
                            contexto.startActivity(intent);
                            break;
                    }
                    return  false;
                });
                popupMenu.show();
            });
        }
    }
}
