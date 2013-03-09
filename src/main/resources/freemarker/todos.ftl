<!DOCTYPE html>

<html>
  <head>
    <title>TODO list</title>
    <style type="text/css">
      .label {text-align: right}
      .error {color: red}
    </style>

  </head>

  <body>
    Known Todos ${todos}
<p>

  <form method="post">
      <table>
          <tr>
              <td class="label">
                  new todo:
              </td>
              <td>
                  <input type="text" name="todo"">
              </td>
              <td class="error">
              </td>
          </tr>
      </table>

      <input type="submit">
  </form>
  </body>

</html>
