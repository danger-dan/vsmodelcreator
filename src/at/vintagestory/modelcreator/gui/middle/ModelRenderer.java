package at.vintagestory.modelcreator.gui.middle;

import static org.lwjgl.opengl.GL11.*;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import at.vintagestory.modelcreator.Camera;
import at.vintagestory.modelcreator.ModelCreator;
import at.vintagestory.modelcreator.enums.EnumFonts;
import at.vintagestory.modelcreator.gui.left.LeftSidebar;
import at.vintagestory.modelcreator.interfaces.IDrawable;
import at.vintagestory.modelcreator.interfaces.IElementManager;
import at.vintagestory.modelcreator.model.Element;
import org.lwjgl.util.glu.Sphere;

public class ModelRenderer
{
	public Camera camera;
	private int width = 990, height = 700;
	public LeftSidebar renderedLeftSidebar = null;
	
	public IElementManager manager;
	
	Sphere sphere = new Sphere();
	
	public ModelRenderer(IElementManager manager) {
		this.manager = manager;
	}
	
	public void Render(int leftSidebarWidth, int width, int height, int frameHeight) {
		this.width = width;
		this.height = height;
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(60F, (float) (width - leftSidebarWidth) / (float) height, 0.3F, 1000F);

		drawGridAndElements();

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);

		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluOrtho2D(0, width, height, 0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		renderLeftPane(leftSidebarWidth, frameHeight);
	}
	
	
	public void drawGridAndElements()
	{
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		camera.useView();
		
		glClearColor(0.92F, 0.92F, 0.93F, 1.0F);
		drawPerspectiveGrid();

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.05f);
		
		glTranslatef(-8, 0, -8);
		
		List<IDrawable> rootelems = ModelCreator.getRootElementsForRender();
		if (rootelems == null) return;
		Element selectedElem = manager.getCurrentElement();

		for (int i = 0; i < rootelems.size(); i++)
		{
			rootelems.get(i).draw(selectedElem);
		}
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		
		if (!ModelCreator.showGrid) return;
		
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glTranslated(0, 0, 16);
			GL11.glScaled(0.018, 0.018, 0.018);
			GL11.glRotated(90, 1, 0, 0);
			EnumFonts.BEBAS_NEUE_50.drawString(8, 0, "VS Model Creator", new Color(0.5F, 0.5F, 0.6F));

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}
	

	
	
	public void drawPerspectiveGrid()
	{
		if (!ModelCreator.showGrid) return;
		
		glPushMatrix();
		{
			glColor3f(0.55F, 0.55F, 0.60F);
			glTranslatef(-8, 0, -8);

			// Bold outside lines
			glLineWidth(2F);
			glBegin(GL_LINES);
			{
				glVertex3i(0, 0, 0);
				glVertex3i(0, 0, 16);
				glVertex3i(16, 0, 0);
				glVertex3i(16, 0, 16);
				glVertex3i(0, 0, 16);
				glVertex3i(16, 0, 16);
				glVertex3i(0, 0, 0);
				glVertex3i(16, 0, 0);
			}
			glEnd();

			// Thin inside lines
			glLineWidth(1F);
			glBegin(GL_LINES);
			{
				for (int i = 1; i <= 16; i++)
				{
					glVertex3i(i, 0, 0);
					glVertex3i(i, 0, 16);
				}

				for (int i = 1; i <= 16; i++)
				{
					glVertex3i(0, 0, i);
					glVertex3i(16, 0, i);
				}
			}
						
			glEnd();
			
			
			// Thin half transparent line to show block size
			glLineWidth(1F);
			glColor3f(0.8F, 0.8F, 0.8F);
			
			
			glBegin(GL_LINES);
			{
				glVertex3i(0, 0, 0);
				glVertex3i(0, 16, 0);
				
				glVertex3i(16, 0, 0);
				glVertex3i(16, 16, 0);

				glVertex3i(16, 0, 16);
				glVertex3i(16, 16, 16);
				
				glVertex3i(0, 0, 16);
				glVertex3i(0, 16, 16);
				
				glVertex3i(0, 16, 0);
				glVertex3i(16, 16, 0);
				
				glVertex3i(16, 16, 0);
				glVertex3i(16, 16, 16);
				
				glVertex3i(16, 16, 16);
				glVertex3i(0, 16, 16);
				
				glVertex3i(0, 16, 16);
				glVertex3i(0, 16, 0);
			}
			glEnd();
			
			

			
		}
		glPopMatrix();
	}


	public void renderLeftPane(int sidebarWidth, int frameHeight)
	{
		glPushMatrix();
		{
			glColor3f(0.58F, 0.58F, 0.58F);
			glLineWidth(2F);
			glBegin(GL_LINES);
			{
				glVertex2i(sidebarWidth, 0);
				glVertex2i(width, 0);
				glVertex2i(width, 0);
				glVertex2i(width, height);
				glVertex2i(sidebarWidth, height);
				glVertex2i(sidebarWidth, 0);
				glVertex2i(sidebarWidth, height);
				glVertex2i(width, height);
			}
			glEnd();
		}
		glPopMatrix();

		if (renderedLeftSidebar != null)
			renderedLeftSidebar.draw(sidebarWidth, width, height, frameHeight);

		
		if (!ModelCreator.showGrid) return;
		
		glPushMatrix();
		{
			glTranslatef(width - 80, height - 80, 0);
			glLineWidth(2F);
			glRotated(-camera.getRY(), 0, 0, 1);

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			EnumFonts.BEBAS_NEUE_20.drawString(-5, -75, "N", new Color(1, 1, 1));
			EnumFonts.BEBAS_NEUE_20.drawString(-5, 55, "S", new Color(1, 1, 1));
			EnumFonts.BEBAS_NEUE_20.drawString(-70, -10, "W", new Color(1, 1, 1));
			EnumFonts.BEBAS_NEUE_20.drawString(55, -10, "E", new Color(1, 1, 1));
			GL11.glDisable(GL11.GL_BLEND);

			glColor3d(0.6, 0.6, 0.6);
			glBegin(GL_LINES);
			{
				glVertex2i(0, -50);
				glVertex2i(0, 50);
				glVertex2i(-50, 0);
				glVertex2i(50, 0);
			}
			glEnd();

			glColor3d(0.3, 0.3, 0.6);
			glBegin(GL_TRIANGLES);
			{
				glVertex2i(-5, -45);
				glVertex2i(0, -50);
				glVertex2i(5, -45);

				glVertex2i(-5, 45);
				glVertex2i(0, 50);
				glVertex2i(5, 45);

				glVertex2i(-45, -5);
				glVertex2i(-50, 0);
				glVertex2i(-45, 5);

				glVertex2i(45, -5);
				glVertex2i(50, 0);
				glVertex2i(45, 5);
			}
			glEnd();
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		glPopMatrix();
	}

	
}
