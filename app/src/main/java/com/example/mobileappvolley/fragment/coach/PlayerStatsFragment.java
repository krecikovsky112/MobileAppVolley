package com.example.mobileappvolley.fragment.coach;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentStatsPlayerBinding;
import com.google.common.util.concurrent.ServiceManager;
import com.google.firebase.auth.FirebaseAuth;

public class PlayerStatsFragment extends Fragment {

    private FragmentStatsPlayerBinding fragmentStatsPlayerBinding;
    private FirebaseAuth firebaseAuth;
    private static final String ARG_PARAM1 = "idUser";
    private static final String ARG_PARAM2 = "attackBlock";
    ServiceManager.Listener mCallback;
    Context mContext;

    public Statistic statisticPlayer = new Statistic();
    public interface SendMessages{
        void send(Statistic statistic);
    }

//    public interface myDataBack {
//        public void bringBack(Long attackBlock);
//    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mContext = context;
//        mCallback = (Listener) mContext;
//    }

    public static PlayerStatsFragment newInstance(String param1) {
        PlayerStatsFragment fragment = new PlayerStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            statisticPlayer.setIdPlayer(getArguments().getString(ARG_PARAM1));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStatsPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats_player, container, false);
        fragmentStatsPlayerBinding.setCallback(this);
        View view = fragmentStatsPlayerBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println(getTag());

        setTextViewsOnStartFragment();

        setListenersStatsAttack();

        setListenersStatsReceive();

        setListenersStatsServe();

        setListenersStatsBlock();

        return view;
    }

    private void setTextViewsOnStartFragment() {
        fragmentStatsPlayerBinding.attackInBlock.setText(statisticPlayer.getBlockAttack().toString());
        fragmentStatsPlayerBinding.attackError.setText(statisticPlayer.getErrorAttack().toString());
        fragmentStatsPlayerBinding.attackPerfect.setText(statisticPlayer.getPerfAttack().toString());
        fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());
        fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());
        fragmentStatsPlayerBinding.receiveError.setText(statisticPlayer.getErrorReceive().toString());
        fragmentStatsPlayerBinding.receiveNegative.setText(statisticPlayer.getNegativeReceive().toString());
        fragmentStatsPlayerBinding.receivePositive.setText(statisticPlayer.getPositiveReceive().toString());
        fragmentStatsPlayerBinding.receivePerfect.setText(statisticPlayer.getPerfReceive().toString());
        fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());
        fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());
        fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
        fragmentStatsPlayerBinding.serveError.setText(statisticPlayer.getErrorServe().toString());
        fragmentStatsPlayerBinding.serveAces.setText(statisticPlayer.getAceServe().toString());
        fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());
        fragmentStatsPlayerBinding.blockAll.setText(statisticPlayer.getAllBlock().toString());
        fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
    }

    private void setListenersStatsBlock() {
        //TODO: All blocks
        fragmentStatsPlayerBinding.plusButtonBlockAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setAllBlock(Long.parseLong(fragmentStatsPlayerBinding.blockAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.blockAll.setText(statisticPlayer.getAllBlock().toString());

                statisticPlayer.setPoints(Integer.parseInt(fragmentStatsPlayerBinding.pointsAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
            }
        });

        fragmentStatsPlayerBinding.minusButtonBlockAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.blockAll.getText().toString()) > 0) {
                    statisticPlayer.setAllBlock(Long.parseLong(fragmentStatsPlayerBinding.blockAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.blockAll.setText(statisticPlayer.getAllBlock().toString());

                    statisticPlayer.setPoints(Integer.parseInt(fragmentStatsPlayerBinding.pointsAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
                }
            }
        });
    }

    private void setListenersStatsServe() {
        //TODO: Error serve
        fragmentStatsPlayerBinding.plusButtonServeError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setErrorServe(Long.parseLong(fragmentStatsPlayerBinding.serveError.getText().toString()) + 1);
                fragmentStatsPlayerBinding.serveError.setText(statisticPlayer.getErrorServe().toString());

                statisticPlayer.setAllServe(Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());

            }
        });

        fragmentStatsPlayerBinding.minusButtonServeError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.serveError.getText().toString()) > 0) {
                    statisticPlayer.setErrorServe(Long.parseLong(fragmentStatsPlayerBinding.serveError.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.serveError.setText(statisticPlayer.getErrorServe().toString());

                    statisticPlayer.setAllServe(Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());
                }
            }
        });

        //TODO: Ace serve
        fragmentStatsPlayerBinding.plusButtonServeAces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setAceServe(Long.parseLong(fragmentStatsPlayerBinding.serveAces.getText().toString()) + 1);
                fragmentStatsPlayerBinding.serveAces.setText(statisticPlayer.getAceServe().toString());

                statisticPlayer.setAllServe(Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());

                statisticPlayer.setPoints(Integer.parseInt(fragmentStatsPlayerBinding.pointsAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
            }
        });

        fragmentStatsPlayerBinding.minusButtonServeAces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.serveAces.getText().toString()) > 0) {
                    statisticPlayer.setAceServe(Long.parseLong(fragmentStatsPlayerBinding.serveAces.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.serveAces.setText(statisticPlayer.getAceServe().toString());

                    statisticPlayer.setAllServe(Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());

                    statisticPlayer.setPoints(Integer.parseInt(fragmentStatsPlayerBinding.pointsAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
                }
            }
        });

        //TODO: All serve
        fragmentStatsPlayerBinding.plusButtonServeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setAllServe(Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());

            }
        });

        fragmentStatsPlayerBinding.minusButtonServeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) > 0) {
                    statisticPlayer.setAllServe(Long.parseLong(fragmentStatsPlayerBinding.serveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.serveAll.setText(statisticPlayer.getAllServe().toString());
                }
            }
        });
    }

    private void setListenersStatsReceive() {
        //TODO: Error receive
        fragmentStatsPlayerBinding.plusButtonReceiveError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setErrorReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveError.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receiveError.setText(statisticPlayer.getErrorReceive().toString());

                statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
            }
        });

        fragmentStatsPlayerBinding.minusButtonReceiveError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.receiveError.getText().toString()) > 0) {
                    statisticPlayer.setErrorReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveError.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receiveError.setText(statisticPlayer.getErrorReceive().toString());

                    statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                    statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                    statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
                }
            }
        });

        //TODO: Negativ receive
        fragmentStatsPlayerBinding.plusButtonReceiveNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setNegativeReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveNegative.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receiveNegative.setText(statisticPlayer.getNegativeReceive().toString());

                statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
            }
        });

        fragmentStatsPlayerBinding.minusButtonReceiveNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.receiveNegative.getText().toString()) > 0) {
                    statisticPlayer.setNegativeReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveNegative.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receiveNegative.setText(statisticPlayer.getNegativeReceive().toString());

                    statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                    statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                    statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
                }
            }
        });

        //TODO: Positive receive
        fragmentStatsPlayerBinding.plusButtonReceivePositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setPositiveReceive(Long.parseLong(fragmentStatsPlayerBinding.receivePositive.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receivePositive.setText(statisticPlayer.getPositiveReceive().toString());

                statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
            }
        });

        fragmentStatsPlayerBinding.minusButtonReceivePositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.receivePositive.getText().toString()) > 0) {
                    statisticPlayer.setPositiveReceive(Long.parseLong(fragmentStatsPlayerBinding.receivePositive.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receivePositive.setText(statisticPlayer.getPositiveReceive().toString());

                    statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                    statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                    statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
                }
            }
        });

        //TODO: Perfect receive
        fragmentStatsPlayerBinding.plusButtonReceivePerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setPerfReceive(Long.parseLong(fragmentStatsPlayerBinding.receivePerfect.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receivePerfect.setText(statisticPlayer.getPerfReceive().toString());

                statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
            }
        });

        fragmentStatsPlayerBinding.minusButtonReceivePerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.receivePerfect.getText().toString()) > 0) {
                    statisticPlayer.setPerfReceive(Long.parseLong(fragmentStatsPlayerBinding.receivePerfect.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receivePerfect.setText(statisticPlayer.getPerfReceive().toString());

                    statisticPlayer.setAllReceive(Long.parseLong(fragmentStatsPlayerBinding.receiveAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.receiveAll.setText(statisticPlayer.getAllReceive().toString());

                    statisticPlayer.setPositiveReceivePerc((long) ((float) statisticPlayer.getPositiveReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePositivePerc.setText(statisticPlayer.getPositiveReceivePerc().toString());

                    statisticPlayer.setPerfReceivePerc((long) ((float) statisticPlayer.getPerfReceive() / statisticPlayer.getAllReceive() * 100));
                    fragmentStatsPlayerBinding.receivePerfectPerc.setText(statisticPlayer.getPerfReceivePerc().toString());
                }
            }
        });
    }

    private void setListenersStatsAttack() {
        //TODO: Attack in block
        fragmentStatsPlayerBinding.plusButtonInBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setBlockAttack(Long.parseLong(fragmentStatsPlayerBinding.attackInBlock.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackInBlock.setText(statisticPlayer.getBlockAttack().toString());

                statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());
                ;
            }
        });

        fragmentStatsPlayerBinding.minusButtonInBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.attackInBlock.getText().toString()) > 0) {
                    statisticPlayer.setBlockAttack(Long.parseLong(fragmentStatsPlayerBinding.attackInBlock.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.attackInBlock.setText(statisticPlayer.getBlockAttack().toString());

                    statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                    statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                    fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());
                }
            }
        });

        //TODO: Attack error
        fragmentStatsPlayerBinding.plusButtonErrorAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setErrorAttack(Long.parseLong(fragmentStatsPlayerBinding.attackError.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackError.setText(statisticPlayer.getErrorAttack().toString());

                statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());
            }
        });

        fragmentStatsPlayerBinding.minusButtonErrorAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.attackError.getText().toString()) > 0) {
                    statisticPlayer.setErrorAttack(Long.parseLong(fragmentStatsPlayerBinding.attackError.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.attackError.setText(statisticPlayer.getErrorAttack().toString());

                    statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                    statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                    fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());
                }
            }
        });

        //TODO: Attack perfect
        fragmentStatsPlayerBinding.plusButtonPerfectAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setPerfAttack(Long.parseLong(fragmentStatsPlayerBinding.attackPerfect.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackPerfect.setText(statisticPlayer.getPerfAttack().toString());

                statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());

                statisticPlayer.setPoints(Integer.parseInt(fragmentStatsPlayerBinding.pointsAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
            }
        });

        fragmentStatsPlayerBinding.minusButtonPerfectAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(fragmentStatsPlayerBinding.attackPerfect.getText().toString()) > 0) {
                    statisticPlayer.setPerfAttack(Long.parseLong(fragmentStatsPlayerBinding.attackPerfect.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.attackPerfect.setText(statisticPlayer.getPerfAttack().toString());

                    statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                    statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                    fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());

                    statisticPlayer.setPoints(Integer.parseInt(fragmentStatsPlayerBinding.pointsAll.getText().toString()) - 1);
                    fragmentStatsPlayerBinding.pointsAll.setText(String.valueOf(statisticPlayer.getPoints()));
                }
            }
        });

        //TODO: Attack all
        fragmentStatsPlayerBinding.plusButtonAllAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticPlayer.setAllAttack(Long.parseLong(fragmentStatsPlayerBinding.attackAll.getText().toString()) + 1);
                fragmentStatsPlayerBinding.attackAll.setText(statisticPlayer.getAllAttack().toString());

                statisticPlayer.setPerfAttackPerc((long) ((float) statisticPlayer.getPerfAttack() / statisticPlayer.getAllAttack() * 100));
                fragmentStatsPlayerBinding.attackPerfectPerc.setText(statisticPlayer.getPerfAttackPerc().toString());
            }
        });
    }
}
