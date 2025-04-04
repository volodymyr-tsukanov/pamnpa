package com.vt.pamnpa.adapters;
import com.vt.pamnpa.R;
import com.vt.pamnpa.room.Element;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ElementRV extends RecyclerView.Adapter<ElementRV.ElementVH> {
    List<Element> elements;
    LayoutInflater li;
    //…
    //w odróżnieniu od lab1c adapter nie otrzymuje listy elementów jako parametru konstruktora
    //w momencie tworzenia obiektu adaptera lista może nie być dostępna
    public ElementRV(Context context) {
        li = LayoutInflater.from(context);
        elements = null;
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
    }

    @Override
    public int getItemCount() {
        //w momencie tworzenia obiektu adaptera lista może nie być dostępna
        if (elements != null)
            return elements.size();
        return 0;
    }
    //ponieważ dane wyświetlane na liście będą się zmieniały ta metoda umożliwia aktualizację
    //danych w adapterze (i w konsekwencji) wyświetlanych w RecyclerView
    public void setElementList(List<Element> elementList) {
        elementList = elementList;
        notifyDataSetChanged();
    }
    //…

    public class ElementVH extends RecyclerView.ViewHolder {
        public ElementVH(View itemView) {
            super(itemView);
        }
    }
}