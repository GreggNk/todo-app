import React, { useEffect } from 'react'
import { useState } from 'react'
import './App.css'
import { Button, Space } from 'antd';

type Todo = {
  id: number
  title: string
  done: boolean
}

function App() {

  const [todoItem, setTodoItem] = useState<string | undefined>();
  const onChange = (e: React.FormEvent<HTMLInputElement>) => setTodoItem(e.currentTarget.value);

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const update = () => {
    if (isLoading)
      return;
    setIsLoading(true)
    fetch('api/get')
      .then((response) => response.json())
      .then((data) => {
        setTodos(data as Todo[])
      })
      .finally(() => setIsLoading(false))
  }

  const save = () => {
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: todoItem })
    };
    fetch('api/save', requestOptions)
      .then(response => response.json())
      .then(() => update())
  };

  const remove = () => {
    fetch('api/remove/' + todoItem, { method: 'DELETE' })
      .then(() => update())
  };

  const [todos, setTodos] = useState<Todo[] | undefined>()

  useEffect(() => {
    update();
  }, []
  )
  //const save = () => fetch('api/save')
  //  .then((data) => {
  //    console.log(data)
  //  });

  //React.useEffect(() => {
  //  const requestOptions = {
  //    method: 'POST',
  //    headers: { 'Content-Type': 'application/json' },
  //    body: JSON.stringify({ title: 'React Hooks POST Request Example' })
  //  };
  //  fetch('api/save', requestOptions)
  //    .then(response => response.json())
  //    //.then(data => setPostId(data.id));
  //}, []);

  return (
    <div className="App">
      <div>
        <label>
          Add new task:
          <input type="text" name="name" onChange={onChange} />
        </label>
        <Space wrap>
          <Button type="primary" value="Save" onClick={save}>Save</Button>
          <Button type="primary" value="Remove" onClick={remove}>Delete</Button>
        </Space>
      </div>
      <div>
        {todos?.map((todos) => <li>{todos.id} - {todos.title} </li>)}
      </div>
    </div>
  )
}

export default App
