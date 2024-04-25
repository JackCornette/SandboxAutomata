package project_1.SandboxEngine.Scene;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Pixel.Special.Conway;
import project_1.SandboxEngine.Pixel.Element.Solid.Sand_pixel;

public class CellularAutomata {

    private static CellularAutomata instance = null;

    //This will hold the current state of the grid. This should only be changed by the draw function
    private Pixel[][] current_grid;

    //Always make changes to this grid.
    private Pixel[][] buffer_grid;

    //These values describe the total draw space.
    private int total_width;
    private int total_height;

    private int total_pixels;

    private final double SQUARE_SIZE = 10.0;

    private CellularAutomata(){
    }

    public static CellularAutomata get(){
        if(CellularAutomata.instance == null){
            CellularAutomata.instance = new CellularAutomata();
        }

        return CellularAutomata.instance;
    }

    public void init(){
        CellularAutomata.get().total_width = (int)(SceneManager.get_width()/CellularAutomata.get().SQUARE_SIZE) - 2;
        CellularAutomata.get().total_height = (int)(SceneManager.get_height()/CellularAutomata.get().SQUARE_SIZE) - 8;
        CellularAutomata.get().total_pixels = CellularAutomata.get().total_height*CellularAutomata.get().total_width;
        CellularAutomata.get().current_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
        CellularAutomata.get().buffer_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
        CellularAutomata.get().empty_curr_grid();
        CellularAutomata.get().empty_buff_grid();

        System.out.println("Cellular Automata Size: "+CellularAutomata.get().total_width+"X"+CellularAutomata.get().total_height);
    }

    public void update(){
        // System.out.println("Cellular Automata Update call");
        CellularAutomata.get().empty_buff_grid();
        for(int col=0; col<CellularAutomata.get().total_width; ++col){
            for(int row=0; row<CellularAutomata.get().total_height; ++row){
                if(CellularAutomata.get().current_grid[col][row] != null){
                    CellularAutomata.get().current_grid[col][row].update();
                }
                //When the conway game of life is being animated, it need to now check all the empty pixel spaces
                if(Conway.is_animating()){
                    
                }
            }
        }
    }

    public void draw(){
        for(int col=0; col<total_width; ++col){
            for(int row=0; row<total_height; ++row){
                if(buffer_grid[col][row] != null){
                    buffer_grid[col][row].draw();
                }
                current_grid[col][row] = buffer_grid[col][row];
            }
        }
    }
        

    public void empty_curr_grid(){
        CellularAutomata.get().current_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
    }

    public void empty_buff_grid(){
        CellularAutomata.get().buffer_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
    }

    //------------------------------------------------------------------------------------------
    //Array functions
    //------------------------------------------------------------------------------------------

    public void add_pixel(Pixel pixel, Vector2d position, boolean buffer_array){
        if(buffer_array){
            CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] = pixel;
        }
        else{
            CellularAutomata.get().current_grid[(int)position.x][(int)position.y] = pixel;
        }
    }

    public void remove_pixel(Vector2d position){
        CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] = null;
    }

    /**
     * Function to check if a pixel at certain position is null
     * 
     * @param position
     * @return
     */
    public boolean pos_empty(Vector2d position, boolean buffer_array){
        if(buffer_array){
            if(CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] == null){
                return true;
            }
            return false;
        }
        else{
            if(CellularAutomata.get().current_grid[(int)position.x][(int)position.y] == null){
                return true;
            }
            return false;
        }
    }

    public boolean pos_allowed(Vector2d position){
        if(position.x<0 || position.x>=CellularAutomata.get().total_width){
            return false;
        }
        if(position.y<0 || position.y>=CellularAutomata.get().total_height){
            return false;
        }
        return true;
    }

    //
    //Game of Life Functions
    //

    // /*
    //  * Converts all of the sand pixels (only enum type implemented currently) to game of life pixels to run the simulation on them.
    //  * Activates when the user clicks the letter 'C', can be found in SceneManager.
    //  */
    // public void convertToGameOfLife() {
    //     System.out.println("called 2");
    //     for (int x = 0; x < get_width(); x++) {
    //         for (int y = 0; y < get_height(); y++) {
    //             Pixel p = current_grid[x][y];
    //             if (p != null && (p instanceof Sand_pixel || p instanceof Conway)) {
    //                 boolean isAlive = true;
    //                 buffer_grid[x][y] = new Conway(PixelType.CONWAY, p.get_ID(), new Vector2d(x, y), isAlive);
    //                 buffer_grid[x][y].set_pixel_color(255, 0, 0); 
    //                 System.out.println(current_grid[x][y]);
    //             } else if (p != null) {
    //                 // Copy non-Sand pixels as they are
    //                 buffer_grid[x][y] = p;
    //             } else {
    //                 buffer_grid[x][y] = null;
    //             }
    //         }
    //     }
    //     // After conversion, you might want to swap grids or copy back as needed
    //     draw();  // This needs to handle buffer properly
    // }
    

    // /*
    //  * The MouseListener() will call this function depending on where the mouse cursor (SceneManager) is when clicked.
    //  */
    // public void togglePixelState(Vector2d position) {
    //     int x = (int)position.x;
    //     int y = (int)position.y;
    //     if (pos_allowed(position) && current_grid[x][y] instanceof Conway) {
    //         Conway pixel = (Conway)current_grid[x][y];
    //         pixel.setAlive(!pixel.getAlive());
    //     }
    // }

    // public void updateConway(){
    //     for (int x = 0; x < get_width(); x++){
    //         for (int y = 0; y < get_height(); y++){
    //             Pixel pixel = current_grid[x][y];
    //             if (pixel instanceof Conway){
    //                 ((Conway) pixel).update();
    //             }
    //         }
    //     }
    // }

    



    //------------------------------------------------------------------------------------------
    //Getter functions
    //------------------------------------------------------------------------------------------

    public int get_height(){
        return CellularAutomata.get().total_height;
    }

    public int get_width(){
        return CellularAutomata.get().total_width;
    }

    public int get_total_pixels(){
        return CellularAutomata.get().total_pixels;
    }

    public Pixel get_pixel(Vector2d position, boolean buffer_array){
        if(buffer_array){
            return CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y];
        }
        else{
            return CellularAutomata.get().current_grid[(int)position.x][(int)position.y];
        }
    }    
}
