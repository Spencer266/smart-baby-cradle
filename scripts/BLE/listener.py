from multiprocessing.connection import Listener

bracelet_addr = ('localhost', 6000)
cradle_addr = ('localhost', 6001)

bracelet_lis = Listener(bracelet_addr, authkey=b'pass')
cradle_lis = Listener(cradle_addr, authkey=b'pass')

bracelet_conn = bracelet_lis.accept()
print("Bracelet process accepted")
cradle_conn = cradle_lis.accept()
print("Cradle process accepted")


while True:
    try:
        if not bracelet_conn.closed:
            br_msg = bracelet_conn.recv()
            if br_msg == 'bclose':
                bracelet_conn.close()
                bracelet_lis.close()
            else:
                print(br_msg)

        if not cradle_conn.closed:
            cr_msg = cradle_conn.recv()
            if cr_msg == 'cclose':
                cradle_conn.close()
                cradle_lis.close()
            else:
                print(cr_msg)

        if cradle_conn.closed and bracelet_conn.closed:
            break
    except KeyboardInterrupt:
        print("Program finished")

    except EOFError:
        print("Connection closed!")
