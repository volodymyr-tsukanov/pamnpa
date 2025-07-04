package com.vt.pamnpa.adapters;
import com.vt.pamnpa.R;
import com.vt.pamnpa.room.Element;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementRV extends RecyclerView.Adapter<ElementRV.ElementVH> {
    public interface OnItemClickListener {
        void onItemClickListener(Element element);
    }

    List<Element> elements;
    LayoutInflater li;
    OnItemClickListener mOnItemClickListener;
    int selectedIndex = -1;
    //…
    //w odróżnieniu od lab1c adapter nie otrzymuje listy elementów jako parametru konstruktora
    //w momencie tworzenia obiektu adaptera lista może nie być dostępna
    public ElementRV(Context context, OnItemClickListener listener) {
        li = LayoutInflater.from(context);
        mOnItemClickListener = listener;
        elements = new ArrayList<>();
    }

    @NonNull
    @Override
    public ElementVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View gradeView = li.inflate(R.layout.list_elements, parent, false);
        return new ElementVH(gradeView);
    }

    @Override
    public void onBindViewHolder(ElementVH holder, int position) {
        Element element = elements.get(position);
        holder.manufacturer.setText(element.getManufacturer());
        holder.model.setText(element.getModel());
        if (selectedIndex == position) {
            holder.bg.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bloody));
        } else {
            holder.bg.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blackTint));
        }
    }

    @Override
    public int getItemCount() {
        //w momencie tworzenia obiektu adaptera lista może nie być dostępna
        return elements.size();
    }
    //ponieważ dane wyświetlane na liście będą się zmieniały ta metoda umożliwia aktualizację
    //danych w adapterze (i w konsekwencji) wyświetlanych w RecyclerView
    public void setElementList(List<Element> elementList) {
        elements = elementList;
        notifyDataSetChanged();
    }
    public void removeElement(int pos){
        elements.remove(pos);
        notifyItemRemoved(pos);
    }
    public void swapElements(int fromPosition, int toPosition) {
        Collections.swap(elements, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
    //…


    public class ElementVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout bg;
        TextView manufacturer, model;

        public ElementVH(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            manufacturer = itemView.findViewById(R.id.manufacturer);
            model = itemView.findViewById(R.id.model);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                int oldSelectedIndex = selectedIndex;
                selectedIndex = selectedIndex==pos ? -1 : pos;
                mOnItemClickListener.onItemClickListener(elements.get(pos));
                notifyItemChanged(pos);
                if(oldSelectedIndex!=-1) notifyItemChanged(oldSelectedIndex);
            }
        }
    }
}