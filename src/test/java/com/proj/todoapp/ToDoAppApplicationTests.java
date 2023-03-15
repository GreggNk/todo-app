package com.proj.todoapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.proj.todoapp.repository.TodoRepo;
import com.proj.todoapp.service.LinkedList;
import com.proj.todoapp.model.TodoItem;

// import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = "com.proj.todoapp")
class ToDoAppApplicationTests {

	@Autowired
	private LinkedList linkedList;

	@Autowired
	private TodoRepo todoRepo;

	@Test
	void contextLoads() {
		assertEquals(2, 1 + 1);
	}

	@Test
	@DirtiesContext
	void testRepoAdd() {
		var item1 = new TodoItem(1L, "A1", "A", 2L, 1L);
		var item2 = new TodoItem(2L, "A2", "A", -1l, 1L);

		todoRepo.save(item1);
		todoRepo.save(item2);
		todoRepo.flush();

		assertEquals(todoRepo.findById(1L).get().getTitle(), "A1");
	}

	@Test
	@DirtiesContext
	void testLinkedList_dragBeginToEnd_TwoElements() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", -1L, 1L));

		// initial condition
		// 1 -> 2
		linkedList.updateDroppable(1L, -1L, "A", 1L);
		// expected order
		// 2 -> 1

		assertNextId(1L, -1L);
		assertNextId(2L, 1L);
	}

	@Test
	@DirtiesContext
	void testLinkedList_dragEndToBegin_TwoElements() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", -1L, 1L));

		// initial condition
		// 1 -> 2
		linkedList.updateDroppable(2L, 1L, "A", 1L);
		// expected order
		// 2 -> 1

		assertNextId(1L, -1L);
		assertNextId(2L, 1L);
	}

	@Test
	@DirtiesContext
	void testLinkedList_dragBeginToEnd_ThreeElements() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", -1L, 1L));

		// initial condition
		// 1 -> 2 -> 3
		linkedList.updateDroppable(1L, -1L, "A", 1L);
		// expected order
		// 2 -> 3 -> 1

		assertNextId(2L, 3L);
		assertNextId(3L, 1L);
		assertNextId(1L, -1L);
	}

	@Test
	@DirtiesContext
	void testLinkedList_dragEndToBegin_ThreeElements() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", -1L, 1L));

		// initial condition
		// 1 -> 2 -> 3
		linkedList.updateDroppable(3L, 1L, "A", 1L);
		// expected order
		// 3 -> 1 -> 2

		assertNextId(3L, 1L);
		assertNextId(1L, 2L);
		assertNextId(2L, -1L);
	}

	@Test
	@DirtiesContext
	void testLinkedList_swapNeighbours_FrontFirst() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", 4L, 1L));
		todoRepo.save(new TodoItem(4L, "A4", "A", 5l, 1L));
		todoRepo.save(new TodoItem(5L, "A5", "A", -1l, 1L));
		// todoRepo.flush();

		// initial condition
		// 1 -> 2 -> 3 -> 4 -> 5
		linkedList.updateDroppable(2L, 4L, "A", 1L);

		// expected order
		// 1 -> 3 -> 2 -> 4 -> 5

		assertNextId(1L, 3L);
		assertNextId(3L, 2L);
		assertNextId(2L, 4L);
		assertNextId(4L, 5L);
		assertNextId(5L, -1l);
	}

	@Test
	@DirtiesContext
	void testLinkedList_swapNeighbours_FrontLast() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", 4L, 1L));
		todoRepo.save(new TodoItem(4L, "A4", "A", 5l, 1L));
		todoRepo.save(new TodoItem(5L, "A5", "A", -1l, 1L));

		// initial condition
		// 1 -> 2 -> 3 -> 4 -> 5
		linkedList.updateDroppable(3L, 2L, "A", 1L);

		// expected order
		// 1 -> 3 -> 2 -> 4 -> 5

		assertNextId(1L, 3L);
		assertNextId(3L, 2L);
		assertNextId(2L, 4L);
		assertNextId(4L, 5L);
		assertNextId(5L, -1l);
	}

	@Test
	@DirtiesContext
	void testLinkedList_DragToEmpty() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", -1L, 1L));

		// initial condition
		// A: 1 -> 2 -> 3
		// B: empty
		linkedList.updateDroppable(3L, -1L, "B", 1L);

		// expected order
		// A: 1 -> 2
		// B: 3

		assertNextId(1L, 2L);
		assertNextId(2L, -1L);
		assertNextId(3L, -1L);
	}

	@Test
	@DirtiesContext
	void testLinkedList_DragToSingle_ToEnd() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", -1L, 1L));
		todoRepo.save(new TodoItem(4L, "B1", "B", -1L, 1L));

		// initial condition
		// A: 1 -> 2 -> 3
		// B: 4
		linkedList.updateDroppable(3L, -1L, "B", 1L);

		// expected order
		// A: 1 -> 2
		// B: 4 -> 3

		assertNextId(1L, 2L);
		assertNextId(2L, -1L);
		assertNextId(4L, 3L);
		assertNextId(3L, -1L);
	}

	@Test
	@DirtiesContext
	void testLinkedList_DragToSingle_ToFront() {
		todoRepo.save(new TodoItem(1L, "A1", "A", 2L, 1L));
		todoRepo.save(new TodoItem(2L, "A2", "A", 3L, 1L));
		todoRepo.save(new TodoItem(3L, "A3", "A", -1L, 1L));
		todoRepo.save(new TodoItem(4L, "B1", "B", -1L, 1L));

		// initial condition
		// A: 1 -> 2 -> 3
		// B: 4
		linkedList.updateDroppable(3L, 4L, "B", 1L);

		// expected order
		// A: 1 -> 2
		// B: 3 -> 4

		assertNextId(1L, 2L);
		assertNextId(2L, -1L);
		assertNextId(3L, 4L);
		assertNextId(4L, -1L);
	}

	void assertNextId(long itemId, Long nextId) {
		assertEquals(nextId, todoRepo.findById(itemId).get().getNextId());
	}

}
