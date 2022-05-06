package com.example.scope.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scope.MainActivity;
import com.example.scope.R;
import com.example.scope.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ListView listView = root.findViewById(R.id.list_planete);

        /*Button stopTrackingBtn = root.findViewById(R.id.stopTrackingBtn);
        stopTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.calculator.stopTracking();
            }
        });*/

        String [] planets;
        planets = getResources().getStringArray(R.array.planets);
        PlanetsAdapter planetsAdapter = new PlanetsAdapter(getActivity(), planets);
        

        listView.setAdapter(planetsAdapter);
        listView.setOnItemClickListener(this);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        //String name = adapterView.getItemAtPosition(position).toString();
        //Toast.makeText(getActivity(), "Item: " + name, Toast.LENGTH_SHORT).show();
            MainActivity.calculator.startTracking(position);
    }
}