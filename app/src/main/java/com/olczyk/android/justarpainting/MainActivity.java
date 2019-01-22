package com.olczyk.android.justarpainting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            placeImageView(arFragment,hitResult);
        });
    }

    private void placeImageView(ArFragment arFragment, HitResult hitResult){
        CompletableFuture<Void> renderableFuture =
                ViewRenderable.builder()
                .setView(arFragment.getContext(),R.layout.painting_module)
//                        .setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER)
                .build()
                .thenAccept(renderable -> {
                    ImageView imageView = (ImageView) renderable.getView();
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    TransformableNode transNode = new TransformableNode(arFragment.getTransformationSystem());
                    transNode.setParent(anchorNode);
                    transNode.setRenderable(renderable);
                    transNode.setLocalPosition(new Vector3(0,1.5f,0));
                    transNode.setLocalRotation(Quaternion.axisAngle(new Vector3(1,0,0),-90));
                    transNode.select();
                });
    }
}
