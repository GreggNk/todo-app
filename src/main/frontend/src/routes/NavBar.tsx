import React, { useState } from 'react';
import { AppstoreOutlined, SettingOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Menu } from 'antd';
import { useUser } from '../UserProvider';

function NavBar() {
  const user = useUser();
  const [current, setCurrent] = useState('mail');

  const items: MenuProps['items'] = React.useMemo(
    () => [
      {
        label: <a href="/">Home</a>,
        key: 'home',
        icon: <AppstoreOutlined />,
      },
      {
        label: <a href="/todoapp">TodoApp</a>,
        key: 'todoApp',
        icon: <SettingOutlined />,
      },
      ...(user == null
        ? [
            {
              label: <a href="/login">Log in</a>,
              key: 'login',
            },
            {
              label: <a href="/signup">Sign up</a>,
              key: 'signin',
            },
          ]
        : []),
    ],
    [user]
  );

  const onClick: MenuProps['onClick'] = (e) => {
    console.log('click ', e);
    setCurrent(e.key);
  };

  return <Menu onClick={onClick} theme="dark" selectedKeys={[current]} mode="horizontal" items={items} />;
}

export default NavBar;
