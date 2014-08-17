package com.cosmicdan.minecraftempires.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import static org.lwjgl.opengl.GL11.*;

public class GuiTextOverlay {
	private static Field alpha_test;
	private Minecraft mc;
	private String header;
	private String description;
	private int tickLimit, totalTicks;

	public GuiTextOverlay(Minecraft mc) {
		this.mc = mc;
	}

	@SubscribeEvent
	public void drawOverlay(RenderGameOverlayEvent event) {
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {      
			return;
		}

		int screenWidth = event.resolution.getScaledWidth();
		int screenHeight = event.resolution.getScaledHeight();
		int screenScale = event.resolution.getScaleFactor();

		// shows overlay every 200 ticks at a duration of 150 ticks
		int ticks = totalTicks - tickLimit + (int) mc.theWorld.getTotalWorldTime();
		if (ticks % 200 == 0) {
			display("Day 3", "Dawn rises", 150);
		}

		if (ticks < totalTicks) {
			double alpha = -((Math.cos(ticks / (double)totalTicks * Math.PI) - 1) * 255.);
			if (alpha > 255) alpha = 255 - alpha % 255;

			// ARGB can't handle low alpha, so break off before it happens
			if ((int)alpha >= 4) {
				int color = 0x00FFFFFF + ((int)(alpha) << 24);

				// small gradient to make text stand out
				drawGradientRect(0, 0, screenWidth, screenHeight, 0x00000000 + ((int)(alpha * 0.8f) << 24), 0x00000000);

				glDisable(GL_ALPHA_TEST);

				setAlphaTest(0xde1);

				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glAlphaFunc(GL_GEQUAL, 0.f);

				// random text position
				glPushMatrix();
				mc.fontRenderer.drawString(description, screenWidth / 2 - mc.fontRenderer.getStringWidth(description) / 2, screenHeight / 5 + mc.fontRenderer.FONT_HEIGHT * 5, color, false);
				glScalef(3.f, 3.f, 1.f);
				mc.fontRenderer.drawString(header, screenWidth / 6 - mc.fontRenderer.getStringWidth(header) / 2, screenHeight / 5 - mc.fontRenderer.FONT_HEIGHT * 3, color, false);
				glPopMatrix();

				setAlphaTest(0xbc0);


				glEnable(GL_ALPHA_TEST);
				glDisable(GL_BLEND);
			}
		}
	}

	//duration is in ticks
	public void display(String header, String description, int duration) {
		this.header = header;
		this.description = description;
		this.totalTicks = duration;
		this.tickLimit = (int) (mc.theWorld.getTotalWorldTime() + duration);
	}

	// copied from Gui
	private void drawGradientRect(double x1, double y1, double x2, double y2, int colorA, int colorB) {
		float f = (float)(colorA >> 24 & 255) / 255.0F;
		float f1 = (float)(colorA >> 16 & 255) / 255.0F;
		float f2 = (float)(colorA >> 8 & 255) / 255.0F;
		float f3 = (float)(colorA & 255) / 255.0F;
		float f4 = (float)(colorB >> 24 & 255) / 255.0F;
		float f5 = (float)(colorB >> 16 & 255) / 255.0F;
		float f6 = (float)(colorB >> 8 & 255) / 255.0F;
		float f7 = (float)(colorB & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(x2, y1, 0.);
		tessellator.addVertex(x1, y1, 0.);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex(x1, y2, 0.);
		tessellator.addVertex(x2, y2, 0.);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	private static void setAlphaTest(int value) {
		try {
			alpha_test.set(null, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	static {
		// really ugly hack to stop the font renderer from enabling alpha
		// testing

		try {
			alpha_test = GL11.class.getDeclaredField("GL_ALPHA_TEST");
			alpha_test.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(alpha_test, alpha_test.getModifiers() & ~Modifier.FINAL);
		} catch(NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}