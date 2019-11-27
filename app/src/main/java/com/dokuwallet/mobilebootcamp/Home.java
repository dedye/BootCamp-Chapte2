package com.dokuwallet.mobilebootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {
    private View view;
    private HomeAdapter adapter;
    private Call<HomeModel> callData;
    private RecyclerView recyclerviewHomeMain;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerviewHomeMain = view.findViewById(R.id.recyclerview_home_main);
        getListData("listDataHome");
    }

    public void getListData(String reguestType) {
        ListApiInterface transactionApiInterface = ServiceGenerator.ApiService(BuildConfig.domainCop);
        callData = transactionApiInterface.ListData(reguestType);
        callData.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                try {
                    if (response.isSuccessful()) {
                        try {
                            if ("0000".equals(response.body().getResponseCode())) {
                                recyclerviewHomeMain.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new HomeAdapter(response.body().getListData());
                                recyclerviewHomeMain.setAdapter(adapter);



                                adapter.SetOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        System.out.println("Dedye Click1 position "+ position);
                                    }
                                });

                            } else {
                                Toast.makeText(getContext(), response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Exception message [" + e.getMessage() + "]");
                        }
                    } else {
                        call.cancel();
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception message [" + e.getMessage() + "]");
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    call.cancel();
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
