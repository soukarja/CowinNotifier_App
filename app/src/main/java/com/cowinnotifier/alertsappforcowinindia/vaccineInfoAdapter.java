package com.cowinnotifier.alertsappforcowinindia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class vaccineInfoAdapter extends RecyclerView.Adapter<vaccineInfoAdapter.ViewHolder> {

    private List<vaccineInfoModel> vaccineInfo;

    public  vaccineInfoAdapter(List<vaccineInfoModel> vaccineInfo){
        this.vaccineInfo = vaccineInfo;
    }

    @NonNull
    @Override
    public vaccineInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_available_row, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vaccineInfoAdapter.ViewHolder holder, int position) {
        String vaccinationAdapterDate=vaccineInfo.get(position).getVaccinationDate();
        String vaccinationAdapterCenter=vaccineInfo.get(position).getVaccinationCenter();
        String vaccinationAdapterAddress=vaccineInfo.get(position).getVaccinationAddress();
        String vaccinationAdapterName=vaccineInfo.get(position).getVaccinationName();
        String vaccinationAdapterAge=vaccineInfo.get(position).getVaccinationAge();
        String vaccinationAdapterPrice=vaccineInfo.get(position).getVaccinationPrice();
        String vaccinationAdapterDose1=vaccineInfo.get(position).getVaccinationDose1();
        String vaccinationAdapterDose2=vaccineInfo.get(position).getVaccinationDose2();

        holder.setData(vaccinationAdapterDate, vaccinationAdapterCenter, vaccinationAdapterAddress, vaccinationAdapterName, vaccinationAdapterAge, vaccinationAdapterPrice, vaccinationAdapterDose1, vaccinationAdapterDose2);

    }

    @Override
    public int getItemCount() {
        return vaccineInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView vaccinationDate;
        private TextView vaccinationCenter;
        private TextView vaccinationAddress;
        private TextView vaccinationName;
        private TextView vaccinationAge;
        private TextView vaccinationPrice;
        private TextView vaccinationDose1;
        private TextView vaccinationDose2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vaccinationDate = itemView.findViewById(R.id.vaccineDate);
            vaccinationCenter = itemView.findViewById(R.id.centerName);
            vaccinationAddress = itemView.findViewById(R.id.centerAddress);
            vaccinationName = itemView.findViewById(R.id.VaccineName);
            vaccinationAge = itemView.findViewById(R.id.VaccineAge);
            vaccinationPrice = itemView.findViewById(R.id.VaccinePrice);
            vaccinationDose1 = itemView.findViewById(R.id.VaccineDose1Btn);
            vaccinationDose2 = itemView.findViewById(R.id.VaccineDose2Btn);

        }

        public void setData(String vaccinationAdapterDate, String vaccinationAdapterCenter, String vaccinationAdapterAddress, String vaccinationAdapterName, String vaccinationAdapterAge, String vaccinationAdapterPrice, String vaccinationAdapterDose1, String vaccinationAdapterDose2) {
            vaccinationDate.setText(vaccinationAdapterDate);
            vaccinationCenter.setText(vaccinationAdapterCenter);
            vaccinationAddress.setText(vaccinationAdapterAddress);
            vaccinationName.setText(vaccinationAdapterName);
            vaccinationAge.setText(vaccinationAdapterAge+"+");
            vaccinationPrice.setText(vaccinationAdapterPrice);
            vaccinationDose1.setText("Dose 1: "+vaccinationAdapterDose1);
            vaccinationDose2.setText("Dose 2: "+vaccinationAdapterDose2);
        }
    }
}
