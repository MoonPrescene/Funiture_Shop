package com.example.funiture_shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.navigation.ui.AppBarConfiguration
import com.example.funiture_shop.databinding.ActivityArBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

@AndroidEntryPoint
class AR_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityArBinding
    lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityArBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = LightEstimationMode.ENVIRONMENTAL_HDR_NO_REFLECTIONS
        }

        placeButton = findViewById(R.id.place)

        placeButton.setOnClickListener {
            placeModel()

        }

        modelNode = ArModelNode(PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/sofa.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)

            )
            {
                sceneView.planeRenderer.isVisible = true
            }
            onAnchorChanged = {
                placeButton.isGone = it != null
            }

        }
        sceneView.addChild(modelNode)
    }

    private fun placeModel(){
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false

    }
}