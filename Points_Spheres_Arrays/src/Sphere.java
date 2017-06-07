class Sphere {
	
	Point center;
	double radius;
	int x,y,z;
	Sphere(int x, int y, int z, double r){
		this.x=x;
		this.y=y;
		this.z=z;
		this.radius=r;
	}
	
	Sphere(Point c, double r){
		this.center=c;
		this.radius=r;
	}
	
	double getX(){
		return this.x;
	}

	double getY(){
		return this.y;
	}

	double getZ(){
		return this.z;
	}


	double getRadius(){
		return this.radius;
	}

	double calculateDiameter(){
		return 2*this.radius;
	}	
	
	double calculatePerimeter(){
		return Math.PI*(this.radius*this.radius);
	}
	
	double calculateVolume(){
		return (3.0/4.0)*Math.PI*(this.radius*this.radius*this.radius); // TODO
	}

}

