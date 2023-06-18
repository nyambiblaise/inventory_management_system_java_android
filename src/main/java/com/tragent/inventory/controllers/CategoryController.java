package com.tragent.inventory.controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tragent.inventory.model.Category;
import com.tragent.inventory.model.Product;
import com.tragent.inventory.service.CategoryService;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * Get all categories.
	 * 
	 * @return all product categories in the system
	 */
	@RequestMapping(method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Category>> getCategories(@RequestParam(value = "name", required = false) String name){
		
		Collection<Category> categories = new ArrayList<Category>();
		if (name != null) {
			Category category = categoryService.findByName(name);
			categories.add(category);
		} else {
			Collection<Category> allCategories = categoryService.findAll();
			categories.addAll(allCategories);
		}
		return new ResponseEntity<Collection<Category>>(categories, HttpStatus.OK);
		
	}
	
	/**
	 * Get category with given category id.
	 * 
	 * @param id category's id
	 * @return the category with given id or 404 if id is not found
	 */
	@RequestMapping(value="/{categoryId}",
			method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId){
		
		Category category = categoryService.findById(categoryId);
		if (category == null) {
			return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Category>(category, HttpStatus.OK);
		
	}
	/**
	 * Get products belonging to category with id.
	 * 
	 * @param id category's id
	 * @return the products belong to category or 404 if id is not found
	 */
	@RequestMapping(value="/{categoryId}/products",
			method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Product>> getProductsformCategory(@PathVariable("categoryId") Long categoryId){
		
		Category category = categoryService.findById(categoryId);
		if (category == null) {
			return new ResponseEntity<Collection<Product>>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Collection<Product>>(category.getProducts(), HttpStatus.OK);
		
	}
	
	/**
	 * Create new category
	 * 
	 * @param category
	 * @return the created category and HttpStatus.CREATED if category was successfully created
	 */
	@RequestMapping(method=RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> createCategory(@RequestBody Category category){
		
		category = categoryService.create(category);
		if (category == null) {
			return new ResponseEntity<Category>(HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<Category>(category, HttpStatus.CREATED);
		
	}
	
	/**
	 * update category's information
	 * 
	 * @param id, category's id
	 * @return the updated category's information.
	 */
	@RequestMapping(value="/{categoryId}",
			method=RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> updateCategory(@RequestBody Category category){
		
		category = categoryService.update(category);
		if (category == null) {
			return new ResponseEntity<Category>(HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<Category>(category, HttpStatus.OK);
		
	}
	
	/**
	 * add product to category.
	 * 
	 * @param categoryId, productId category's id and product's id
	 * @return the updated list of products belong to category
	 */
	@RequestMapping(value="/{categoryId}/products/{productId}",
			method=RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Product>> addProductToCategory(@PathVariable("categoryId") Long categoryId, @PathVariable("productId") Long productId){
		
		Collection<Product> products = categoryService.addProduct(categoryId, productId);
		if (products == null) {
			return new ResponseEntity<Collection<Product>>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Collection<Product>>(products, HttpStatus.OK); 
		
	}
	
	/**
	 * Delete category's information
	 * 
	 * @param id, category's id
	 * @return 204, .
	 */
	@RequestMapping(value="/{categoryId}",
			method=RequestMethod.DELETE)
	public ResponseEntity<Category> deleteCategory(@PathVariable("categoryId") Long categoryId){
		
		categoryService.delete(categoryId);
		return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
		
	}

}
